(ns leiningen.commit
  (:require [clojure.string :as str]
            [leiningen.command :as c]))

(defn get-user-input-with [question]
  (println question)
  (read-line))

(defn- with-bracket [s] (str "[" s "]"))

(defn commit-msg [answers]
  (if (some str/blank? answers)
    ""
    (let [answer-with-bracket (reduce #(str %1 " " %2) (map with-bracket answers))]
      (with-bracket answer-with-bracket))))

(defn parse-specification [specification]
  (map #(name %) specification))

(defn commit-program [spec]
  (println "The commit will follow the pattern: " spec)
  (let [questions (parse-specification spec)
        answers (map #(get-user-input-with (str "\nPlease enter the " %)) questions)]
    (c/commit (commit-msg answers))))
