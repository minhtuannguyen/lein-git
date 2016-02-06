(ns leiningen.command
  (:require [clojure.java.shell :as sh]
            [clojure.data.json :as json]
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

(defn remove-from-end [s end]
  (if (.endsWith s end)
    (.substring s 0 (- (count s)
                       (count end)))
    s))


(defn parse-log [s]
  (let [clean-s (str/replace s #"\'" "")
        s-w-o-last-comma (remove-from-end clean-s ",")
        complete-s (str "[" (str/replace s-w-o-last-comma #"\'" "") "]")]
    (json/read-str complete-s :key-fn keyword)))

(defn log []
  (let [pretty-format "--pretty=format:'{\"commit\": \"%H\",%n \"message\": \"%f\"},'"
        result (sh/sh "git" "log" pretty-format)]
    (if (is-success result)
      (parse-log (:out result))
      (str "failed " (:status result)))))