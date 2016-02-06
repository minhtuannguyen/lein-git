(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.specification :as s]
            [leiningen.utils :as u]
            [leiningen.core.main :as main]))

(defn- log-map-of [extra-content spec]
  (let [all-spec-names (s/get-all-spec-names spec)
        required-spec-name (s/get-required-spec-names spec)]
    (cond (= (count extra-content) (count all-spec-names))
          (zipmap all-spec-names extra-content)
          (= (count extra-content) (count required-spec-name))
          (zipmap required-spec-name extra-content)
          :else {})))

(defn- safe-read-msg [s spec]
  (try
    (let [log-entries (map str (read-string s))
          commit-msg (first (reverse log-entries))
          extra-content (drop-last log-entries)]
      (-> extra-content
          (log-map-of spec)
          (assoc :commit-msg commit-msg)))
    (catch Exception _
      {:commit-msg s})))

(defn- log->edn [{commit :commit message-str :message} spec]
  {:commit  commit
   :message (safe-read-msg message-str spec)})

(defn get-structured-logs [spec git-logs-fnc]
  (let [logs (git-logs-fnc)]
    (map #(log->edn % spec) logs)))

(defn display-search-option [[first second]]
  (str "*" second "(" first ")\n"))

(defn- is-input-valid? [s]
  (try
    (Integer/parseInt s)
    true
    (catch Exception _
      false)))

(defn run [spec]
  (println "Select the field you want to search for: ")
  (let [indexed-spec-names (map-indexed vector (s/get-all-spec-names spec))
        question (reduce str (map display-search-option indexed-spec-names))
        user-decision (u/get-validated-input question is-input-valid?)
        chosen-spec (nth indexed-spec-names (Integer/parseInt user-decision))
        db (get-structured-logs spec c/get-logs)]

    (println "you choose " chosen-spec)))
