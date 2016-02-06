(ns leiningen.specification
  (:require [clojure.string :as str]))

(defn required-input [input] (not (str/blank? input)))
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

(defn parse [specification]
  (map #(parse-spec-entry %) specification))

(defn get-spec-names [specification]
  (map #(first %) specification))

(defn valid? [spec]
  (not (nil? spec)))
