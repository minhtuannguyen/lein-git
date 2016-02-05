(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [clojure.string :as str]
            [leiningen.command :as c]))

(def usage-msg "Usage: lein git-msg\nThe current dir must be a git repository")

(defn get-user-input-with [question]
  (println question)
  (read-line))

(defn commit-msg [story-id software msg]
  (if (or (str/blank? story-id) (str/blank? software) (str/blank? msg))
    ""
    (str "[" story-id "] " "[" software "] " msg)))

(defn commit-program []
  (let [story-id (get-user-input-with "Enter the story id")
        software (get-user-input-with "Enter the software component")
        msg (get-user-input-with "Enter the commit message")]
    (c/commit (commit-msg story-id software msg))))

(defn one-arg-program [param1]
  (cond
    (= param1 "commit") (commit-program)
    :else (println "unknown option"))
  (main/exit))

(defn git
  [project & args]
  (when (not (c/is-git-repository))
    (main/abort usage-msg))

  (when (= 1 (count args))
    (one-arg-program (first args)))

  (main/abort usage-msg))
