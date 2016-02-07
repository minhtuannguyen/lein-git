(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.specification :as s]
            [leiningen.utils :as u]))

(def ask-for-query-str "\nEnter your query:")

(defn- logs-map-of [extra-content spec]
  (let [all-spec-names (s/get-all-spec-names spec)
        required-spec-names (s/get-required-spec-names spec)]
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
  (str "**** " second " -> (Enter " first ")\n"))

(defn- is-input-valid? [s]
  (try
    (Integer/parseInt s)
    true
    (catch Exception _
      false)))

(defn- matches? [entry spec-name query]
  (let [content (get-in entry [:message spec-name])]
    (and (not (nil? content)) (.contains content query))))

(defn- search [db query spec-name]
  (filter #(matches? % spec-name query) db))

(defn- print-result [results spec query]
  (println "\nRESULT FOR YOUR QUERY " spec "=" query "\n
||=================COMMIT==================||===================MESSAGE===================")
  (let [result-representation (map #(str "||" (:commit %) " || " (:message %) "||\n") results)]
    (println (reduce str result-representation))
    (println "\n")))

(defn do [spec]
  (println "Select the field you want to search for: ")
  (let [indexed-spec (map-indexed vector (s/get-all-spec-names spec))
        question (reduce str (map search-option-display-str indexed-spec))
        user-spec-decision (u/get-validated-input question is-input-valid?)
        chosen-spec-name (second (nth indexed-spec (Integer/parseInt user-spec-decision)))
        user-query (u/get-validated-input ask-for-query-str u/not-blank?)
        db (structured-logs-of spec c/get-logs)
        search-result (search db user-query chosen-spec-name)]
    (if (= 0 (count search-result))
      (println "NO COMMIT MATCHES YOUR QUERY " chosen-spec-name "=" user-query)
      (print-result search-result chosen-spec-name user-query))))