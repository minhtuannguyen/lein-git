(ns leiningen.commit
  (:require [clojure.string :as str]
            [leiningen.command :as c]))

(def commit c/commit)

(defn required-input [input] (not (str/blank? input)))
(defn optinal-input [_] true)

(defn get-input [question]
  (println (str "\nPlease enter the " question ":"))
  (read-line))

(defn get-validated-input [{question :question validate-fnc :validate-fn}]
  (loop [input (get-input question)]
    (if (validate-fnc input)
      input
      (do
        (println "the input was not correct")
        (recur (get-input question))))))

(defn- ask-user-with [questions]
  (map #(get-validated-input %) questions))

(defn- with-bracket [s]
  (if (str/blank? s)
    ""
    (str "[" s "]")))

(defn commit-msg [answers]
  (let [answer-with-bracket (reduce #(str %1 %2) (map with-bracket answers))]
    (with-bracket answer-with-bracket)))

(defn- detect-validate-fnc [constraint-name]
  (cond
    (= (name constraint-name) "required") required-input
    (= (name constraint-name) "optional") optinal-input
    :else optinal-input))

(defn- parse-spec-entry [[first second]]
  (let [spec-name (name first)
        constraint-fnc (detect-validate-fnc second)]
    {:question spec-name :validate-fn constraint-fnc}))

(defn parse [specification]
  (map #(parse-spec-entry %) specification))

(defn run [spec]
  (println "The commit will follow the pattern: " spec)
  (let [questions (parse spec)
        answers (ask-user-with questions)
        commit-msg (commit-msg answers)]
    (commit commit-msg)))