(ns leiningen.csearch
  (:require [leiningen.command :as c]))

(defn get-input []
  (println "\nPlease enter the edn search string")
  (read-line))

(defn query-string []
  (let [query-string (get-input)
        query (read-string query-string)]
    (println query)))

(defn run [specification]
  (println "All commits follow the pattern " specification)
  (let [logs (c/log)]))
