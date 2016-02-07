(ns leiningen.specification
  (:require [leiningen.utils :as u]))

(defn required-input [input] (u/not-blank? input))
(defn optinal-input [_] true)

(defn- detect-validate-fnc [constraint-name]
  (cond
    (= constraint-name :required) required-input
    (= constraint-name :optional) optinal-input
    :else optinal-input))

(defn- parse-spec-entry [[first second]]
  (let [spec-name (name first)
        constraint-fnc (detect-validate-fnc second)]
    {:question spec-name :validate-fn constraint-fnc}))

(defn parse [spec]
  (map #(parse-spec-entry %) spec))

(defn get-all-spec-names [spec]
  (map #(first %) spec))

(defn get-required-spec-names [spec]
  (get-all-spec-names
    (filter #(= (second %) :required) spec)))

(defn valid? [spec]
  (not (nil? spec)))
