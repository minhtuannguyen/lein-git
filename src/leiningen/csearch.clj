(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.specification :as s]))

(defn- log-map-of [extra-content spec]
  (let [spec-names (s/get-spec-names spec)]
    (if (= (count extra-content) (count spec-names))
      (zipmap spec-names extra-content)
      {})))

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

(defn run [search-query spec]
  (println (get-structured-logs spec c/get-logs)))
