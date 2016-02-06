(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [leiningen.commit :as commit]
            [leiningen.command :as command]
            [leiningen.csearch :as search]))

(def usage-msg "Usage: lein git commit -m message\n
The current dir must be a git repository.
Moreover, you must specify :lein-git-spec in your project.clj")

(defn commit-program [params specification]
  (cond
    (or (not= (count params) 2) (not= (first params) "-m")) (main/abort usage-msg)
    :else (commit/run (second params) specification))
  (main/exit))

(defn git
  [project & args]
  (println args)
  (when (or
          (not (command/is-git-repository))
          (nil? (:lein-git-spec project)))
    (main/abort usage-msg))


  (when (and (>= 3 (count args)) (= (first args) "commit"))
    (commit-program (rest args) (:lein-git-spec project)))

  (main/abort usage-msg))