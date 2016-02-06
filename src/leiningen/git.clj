(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [leiningen.commit :as commit]
            [leiningen.utils :as u]
            [leiningen.csearch :as search]
            [leiningen.specification :as spec]))

(def usage-msg "Usage: lein git commit -m message\n
The current dir must be a git repository.
Moreover, you must specify :lein-git-spec in your project.clj")

(defn commit-program [params spec]
  (cond
    (or (not= (count params) 2) (not= (first params) "-m"))
    (main/abort usage-msg)
    :else (commit/run (second params) spec))
  (main/exit))

(defn search-program [query spec]
  (search/run query spec)
  (main/exit))

(defn git
  [project & args]
  (let [spec (:lein-git-spec project)]
    (if (and
          (u/is-git-repository)
          (spec/valid? spec))
      (println "All commits follow the spec: " spec)
      (main/abort usage-msg))

    (when (and (>= 3 (count args)) (= (first args) "commit"))
      (commit-program (rest args) spec))

    (when (and (>= 2 (count args)) (= (first args) "search"))
      (search-program (second args) spec))

    (main/abort usage-msg)))