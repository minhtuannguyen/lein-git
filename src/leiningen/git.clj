(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [clojure.string :as str]
            [leiningen.command :as c]))

(def usage-msg "Usage: lein git-msg\nThe current dir must be a git repository")

(defn get-user-input-with [question]
  (println question)
  (read-line))

(defn- with-bracket [s] (str "[" s "]"))

(defn commit-msg [answers]
  (if (some str/blank? answers)
    ""
    (let [answer-with-bracket (reduce #(str %1 " " %2) (map with-bracket answers))]
      (with-bracket answer-with-bracket))))

(defn commit-program [specification]
  (let [questions (map #(name %) specification)
        answers (map #(get-user-input-with (str "Please enter the " %)) questions)]
    (c/commit (commit-msg answers))))

(defn one-arg-program [param specification]
  (cond
    (= param "commit") (commit-program specification)
    :else (println "unknown option"))
  (main/exit))

(defn git
  [project & args]
  (when (not (c/is-git-repository))
    (main/abort usage-msg))

  (when (= 1 (count args))
    (one-arg-program (first args) (:lein-git-spec project)))

  (main/abort usage-msg))
