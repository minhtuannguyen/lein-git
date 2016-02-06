(ns leiningen.utils
  (:require [clojure.java.shell :as sh]))

(defn is-success [shell-result]
  (= (:exit shell-result) 0))

(defn remove-from-end [s end]
  (if (.endsWith s end)
    (.substring s 0 (- (count s) (count end)))
    s))

(defn get-input [question]
  (println (str "\nPlease enter the " question ":"))
  (read-line))

(defn is-git-repository []
  (let [result (sh/sh "git" "rev-parse")]
    (is-success result)))
