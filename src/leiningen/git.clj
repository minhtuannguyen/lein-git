(ns leiningen.git
  (:require [leiningen.core.main :as main]
            [leiningen.commit :as commit]
            [leiningen.utils :as u]
            [leiningen.csearch :as search]
            [leiningen.specification :as spec]))

(def usage-msg "
Usage:
to commit: lein git commit [-m -am] \"message\"
to search: lein git search
to see spec: lein git spec
The current dir must be a git repository.
Moreover, you must specify :lein-git-spec in your project.clj")

(defn print-found [spec] (println "The lein-git-spec found in the project: " spec "\n"))

(defn commit-program [[first-arg second-arg] spec commit-fnc]
  (cond
    (or (= first-arg "-m") (= first-arg "-am"))
    (commit-fnc second-arg spec)
    :else (main/abort usage-msg)))

(defn search-program [spec search-fnc]
  (search-fnc spec))

(defn- main-program [args spec]

  (when (and (= 1 (count args)) (= (first args) "spec"))
    (print-found spec))

  (when (and (= 1 (count args)) (= (first args) "search"))
    (search-program spec search/do))

  (when (and (= 3 (count args)) (= (first args) "commit"))
    (commit-program (rest args) spec commit/do))

  (main/exit))

(defn- project-valid? [spec]
  (cond
    (not (u/is-git-repository?))
    (do (println "It is not a git repository") false)
    (not (spec/valid? spec))
    (do (println ":lein-git-spec has not been defined in the project") false)
    :else true))

(defn git
  [project & args]
  (let [spec (:lein-git-spec project)]
    (if (project-valid? spec)
      (main-program args spec)
      (main/abort usage-msg))))