(ns leiningen.specification
  (:require [leiningen.utils :as u]))

(defn required-input [input] (u/not-blank? input))
(defn optional-input [_] true)

(defn- detect-validate-fnc [constraint-name]
  (cond
    (= constraint-name :required) required-input
    (= constraint-name :optional) optional-input
    :else optional-input))

(defn- parse-spec-entry [[first second]]
  (let [spec-name (name first)
        constraint-fnc (detect-validate-fnc second)]
    {:question spec-name :validate-fn constraint-fnc}))

(defn parse [spec]
  (map #(parse-spec-entry %) spec))

(defn all-names-of [spec]
  (map #(first %) spec))

(defn required-names-of [spec]
  (all-names-of
    (filter #(= (second %) :required) spec)))

(defn valid? [spec]
  (not (nil? spec)))
