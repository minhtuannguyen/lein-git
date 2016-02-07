(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [leiningen.commit :as commit]
            [leiningen.utils :as u]
            [leiningen.csearch :as search]
            [leiningen.specification :as spec]))

(def usage-msg "Usage:
to commit: lein git commit -m \"message\"
to search: lein search
The current dir must be a git repository.
Moreover, you must specify :lein-git-spec in your project.clj")

(defn commit-program [params spec]
  (cond
    (or (not= (count params) 2) (not= (first params) "-m"))
    (main/abort usage-msg)
    :else (commit/run (second params) spec))
  (main/exit))

(defn search-program [spec]
  (search/run spec)
  (main/exit))

(defn git
  [project & args]
  (let [spec (:lein-git-spec project)]
    (if (and
          (u/is-git-repository)
          (spec/valid? spec))
      (println "The lein-git-spec found in the project: " spec "\n")
      (main/abort usage-msg))

    (when (and (>= 3 (count args)) (= (first args) "commit"))
      (commit-program (rest args) spec))

    (when (and (>= 1 (count args)) (= (first args) "search"))
      (search-program spec))

    (main/abort usage-msg)))