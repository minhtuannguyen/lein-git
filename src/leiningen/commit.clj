(ns leiningen.commit
  (:require [clojure.string :as str]
            [leiningen.command :as c]))

(defn- get-user-input-with [question]
  (println question)
  (read-line))

(defn- ask-user-with [questions]
  (map #(get-user-input-with (str "\nPlease enter the " % ":")) questions))

(defn- with-bracket [s] (str "[" s "]"))

(defn- build-commit-msg-from [answers]
  (let [answer-with-bracket (reduce #(str %1 " " %2) (map with-bracket answers))]
    (with-bracket answer-with-bracket)))

(defn commit-msg [answers]
  (if (some str/blank? answers)
    ""
    (build-commit-msg-from answers)))

(defn parse-specification [specification]
  (map #(name %) specification))

(defn run [spec]
  (println "The commit will follow the pattern: " spec)
  (let [questions (parse-specification spec)
        answers (ask-user-with questions)
        commit-msg (commit-msg answers)]
    (c/commit commit-msg)))
