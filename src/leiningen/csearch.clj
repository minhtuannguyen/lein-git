(ns leiningen.csearch)

(defn get-input []
  (println "\nPlease enter the edn search string")
  (read-line))

(defn run [specification]
  (println "All commits follow the pattern " specification)
  (let [query-string (get-input)
        query (read-string query-string)]
    (println query)))
