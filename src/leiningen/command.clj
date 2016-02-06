(ns leiningen.command
  (:require [clojure.java.shell :as sh]
            [clojure.data.json :as json]
            [leiningen.utils :as u]
            [clojure.string :as str]))
(def json-format "\"--pretty=format:'{\\\"commit\\\": \\\"%H\\\",%n \\\"message\\\": \\\"%f\\\"},'\"")

(defn commit-with [message shell-fnc]
  (if (str/blank? message)
    "Commit message can not be empty"
    (let [result (shell-fnc "git" "commit" "-m" message)]
      (if (u/is-success result)
        (str "Commited with the message: " message)
        (str "Commit has been rejected with the reason: " (:out result))))))

(defn commit [message]
  (let [result (commit-with message sh/sh)]
    (println result)))

(defn log->json [s]
  (let [clean-s (str/replace s #"\'" "")
        s-w-o-last-comma (u/remove-from-end clean-s ",")
        complete-s (str "[" (str/replace s-w-o-last-comma #"\'" "") "]")]
    (json/read-str complete-s :key-fn keyword)))

(defn get-logs []
  (let [result (sh/sh "git" "log" json-format)]
    (if (u/is-success result)
      (log->json (:out result))
      {:failed (:status result)})))