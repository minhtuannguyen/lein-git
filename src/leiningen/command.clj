(ns leiningen.command
  (:require [clojure.java.shell :as sh]
            [clojure.data.json :as json]
            [leiningen.utils :as u]
            [clojure.string :as str]))

(def json-format "--pretty=format:'{####====commit####==== : ####====%H####====,####====message####==== : ####====%s####====},'")

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
  (let [wo-singe_quote (str/replace s #"\'" "")
        wo-double-quote (str/replace wo-singe_quote #"\"" "")
        convert-json-magic-to-doube-quoute (str/replace wo-double-quote #"####====" "\"")
        wo-last-singe-quoute (u/remove-from-end convert-json-magic-to-doube-quoute ",")
        complete-s (str "[" (str/replace wo-last-singe-quoute #"\'" "") "]")]
    (json/read-str complete-s :key-fn keyword)))

(defn get-logs []
  (let [result (sh/sh "git" "log" json-format)]
    (if (u/is-success result)
      (log->json (:out result))
      {})))