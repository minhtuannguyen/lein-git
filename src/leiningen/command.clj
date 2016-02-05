(ns leiningen.command
  (:require [clojure.java.shell :as sh]))

(defn- is-success [shell-result]
  (= (:exit shell-result) 0))

(defn is-git-repository []
  (let [result (sh/sh "git" "rev-parse")]
    (is-success result)))

(defn commit [message]
  (let [result (sh/sh "git" "commit" "-m" message)]
    (if (is-success result)
      (println "Commited with the message: " message)
      (println "Commit has been rejected with the reason" (:out result)))))

