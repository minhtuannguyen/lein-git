(ns leiningen.csearch
  (:require [leiningen.command :as c]
            [leiningen.specification :as s]))

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

(defn run [search-query spec]
  (println (get-structured-logs spec c/get-logs)))
