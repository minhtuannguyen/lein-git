(ns leiningen.command
  (:require [clojure.java.shell :as sh]
            [clojure.string :as str]))

(defn- is-success [shell-result]
  (= (:exit shell-result) 0))

(defn is-git-repository []
  (let [result (sh/sh "git" "rev-parse")]
    (is-success result)))

(defn commit-with [message shell-fnc]
  (if (str/blank? message)
    "Commit message can not be empty"
    (let [result (shell-fnc "git" "commit" "-m" message)]
      (if (is-success result)
        (str "Commited with the message: " message)
        (str "Commit has been rejected with the reason: " (:out result))))))

(defn commit [message]
  (let [result (commit-with message sh/sh)]
    (println result)))

