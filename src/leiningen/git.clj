(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [leiningen.command :as c]))

(def usage-msg "Usage: lein git-msg\nThe current dir must be a git repository")

(defn one-arg-program [param1 param2]
  (cond
    (= param1 "commit") (c/commit param2)
    :else (println "unknown option"))
  (main/exit))

(defn git
  [project & args]
  (when (not (c/is-git-repository))
    (main/abort usage-msg))

  (when (= 2 (count args))
    (one-arg-program (first args) (second args)))

  (main/abort usage-msg))
