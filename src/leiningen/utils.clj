(ns leiningen.utils
  (:require [clojure.java.shell :as sh]))

(defn is-success [shell-result]
  (= (:exit shell-result) 0))

(defn remove-from-end [s end]
  (if (.endsWith s end)
    (.substring s 0 (- (count s) (count end)))
    s))

(defn is-git-repository []
  (let [result (sh/sh "git" "rev-parse")]
    (is-success result)))

(defn get-input [prompt]
  (println prompt)
  (read-line))

(defn get-validated-input [question validate-fnc]
  (loop [input (get-input question)]
    (if (validate-fnc input)
      input
      (do
        (println "the input was not correct")
        (recur (get-input question))))))