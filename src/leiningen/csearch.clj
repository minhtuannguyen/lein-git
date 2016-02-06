(ns leiningen.csearch
  (:require [leiningen.command :as c]))

(defn safe-read-msg [s spec]
  (try
    (let [log-entries (read-string s)
          extra-content (subvec log-entries 0 (dec (count log-entries)))
          commit-msg (first (reverse log-entries))]
      (if (= (count extra-content) (count spec))
        {:commit-msg    commit-msg
         :extra-content extra-content}
        {:commit-msg commit-msg}))
    (catch Exception _
      {:commit-msg s})))

(defn log->edn [{commit :commit message-str :message} spec]
  {:commit  commit
   :message (safe-read-msg message-str spec)})

(defn run [search-query spec]
  (let [logs (c/get-logs)
        parsed-logs (map #(log->edn % spec) logs)]
    (println parsed-logs)))
