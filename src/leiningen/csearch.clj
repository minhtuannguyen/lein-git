(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.specification :as s]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [leiningen.utils :as u]))

(def ask-for-query "\nEnter your query:")

(defn- logs-map-of [extra-content spec]
  (let [all-spec-names (s/all-names-of spec)
        required-spec-names (s/required-names-of spec)]
    (cond
      (= (count extra-content) (count all-spec-names))
      (zipmap all-spec-names extra-content)
      (= (count extra-content) (count required-spec-names))
      (zipmap required-spec-names extra-content)
      :else {})))

(defn- safe-read-msg [s spec]
  (try
    (let [log-entries (map str (read-string s))
          commit-msg (first (reverse log-entries))
          extra-content (drop-last log-entries)]
      (-> extra-content
          (logs-map-of spec)
          (assoc :commit-msg commit-msg)))
    (catch Exception _
      {:commit-msg s})))

(defn- log->edn [{commit :commit message-str :message} spec]
  {:commit  commit
   :message (safe-read-msg message-str spec)})

(defn structured-logs-of [spec git-logs-fnc]
  (let [logs (git-logs-fnc)]
    (map #(log->edn % spec) logs)))

(defn- search-option-display-str [[first second]]
  (str "*** " second " -> (Enter " first ")\n"))

(defn- input-valid? [s]
  (try
    (Integer/parseInt s)
    true
    (catch Exception _
      false)))

(defn- matches? [entry spec-name query]
  (let [content (get-in entry [:message spec-name])]
    (and
      (not (nil? content))
      (or (.contains content query)
          (.contains (str/lower-case content) query)))))

(defn pp-entry-fnc [result]
  (-> (:message result)
      (assoc :commit (:commit result))))

(defn pp-fnc [results]
  (map pp-entry-fnc results))

(defn search [db query spec-name]
  (filter #(matches? % spec-name query) db))

(defn do-search [query spec-name spec]
  (println "For your query " spec-name "=" query ": ")
  (let [db (structured-logs-of spec c/get-logs)
        search-result (search db query spec-name)]
    (if (= 0 (count search-result))
      (println "No commit matches your query")
      (pp/print-table (pp-fnc search-result)))))

(defn- parse-query [query]
  (try (str/split query #"=")
       (catch Exception _
         [nil nil])))

(defn do-in-interactive-mode [spec]
  (println "Select the field you want to search for: ")
  (let [indexed-spec (map-indexed vector (s/all-names-of spec))
        question (reduce str (map search-option-display-str indexed-spec))
        user-spec (u/get-validated-input question input-valid?)
        user-spec-name (second (nth indexed-spec (Integer/parseInt user-spec)))
        user-query (u/get-validated-input ask-for-query u/not-blank?)]
    (do-search user-query user-spec-name spec)))

(defn do-in-query-mode [query spec]
  (let [[spec-key spec-value] (parse-query query)]
    (if (or (nil? spec-key) (nil? spec-value))
      (println "query is not correct")
      (do-search spec-value (keyword spec-key) spec))))