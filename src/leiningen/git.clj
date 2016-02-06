(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [leiningen.commit :as commit]
            [leiningen.command :as command]))

(def usage-msg "Usage: lein git-msg commit\n
The current dir must be a git repository.
Moreover, you must specify :lein-git-spec in your project.clj")

(defn one-arg-program [param specification]
  (cond
    (= param "commit") (commit/commit-program specification)
    (= param "search") (main/abort "not implement yet")
    :else (main/abort usage-msg))
  (main/exit))

(defn git
  [project & args]
  (when (or
          (not (command/is-git-repository))
          (nil? (:lein-git-spec project)))
    (main/abort usage-msg))

  (when (= 1 (count args))
    (one-arg-program (first args) (:lein-git-spec project)))

  (main/abort usage-msg))