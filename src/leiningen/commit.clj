(ns leiningen.commit
  (:require [clojure.string :as str]
            [leiningen.specification :as spec]
            [leiningen.utils :as u]
            [leiningen.command :as c]))

(def commit c/commit)

(defn get-validated-input [{question :question validate-fnc :validate-fn}]
  (u/get-validated-input (str "\nPlease enter the " question ":") validate-fnc))

(defn- ask-user-with [questions]
  (map #(get-validated-input %) questions))

(defn- with-bracket [s]
  (if (str/blank? s)
    ""
    (str " [" s "]")))

(defn commit-msg [answers msg]
  (let [answer-with-bracket (reduce #(str %1 %2) (map with-bracket answers))]
    (str "[" answer-with-bracket " [" msg "] ]")))

(defn run [msg spec]
  (let [questions (spec/parse spec)
        answers (ask-user-with questions)
        commit-msg (commit-msg answers msg)]
    (commit commit-msg)))