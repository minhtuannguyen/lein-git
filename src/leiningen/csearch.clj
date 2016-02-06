(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.utils :as u]))

(defn query-string []
  (let [query-string (u/get-input " query")
        query (read-string query-string)]
    (println query)))

(defn safe-read-msg [s spec]
  (try
    (let [log-entries (read-string s)]
      ;(if (= (count log-entries) (count spec)))
      )
    (catch Exception _
      [:raw-message s])))

(defn log->edn [{commit :commit message-str :message} spec]
  (let [parsed-msg (safe-read-msg message-str spec)]
    {:commit commit :message parsed-msg}))

(defn run [search-query spec]
  (let [logs (c/get-logs)]
    ;(map #(log->edn % spec) (log->json (:out result)))
    (println logs)))
