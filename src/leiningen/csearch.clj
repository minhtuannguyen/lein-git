(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.specification :as s]))

(defn build-map-from-log-entries [extra-content spec]
  (let [spec-names (s/get-spec-names spec)]
    (if (= (count extra-content) (count spec-names))
      (zipmap spec-names extra-content)
      {})))

(defn safe-read-msg [s spec]
  (try
    (let [log-entries (read-string s)
          commit-msg (first (reverse log-entries))
          extra-content (drop-last log-entries)
          extra-content-map (build-map-from-log-entries extra-content spec)]
      (assoc extra-content-map :commit-msg commit-msg))
    (catch Exception _
      {:commit-msg s})))

(defn log->edn [{commit :commit message-str :message} spec]
  {:commit  commit
   :message (safe-read-msg message-str spec)})

(defn run [search-query spec]
  (let [logs (c/get-logs)
        parsed-logs (map #(log->edn % spec) logs)]
    ;(println parsed-logs)
    ))
