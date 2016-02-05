(ns leiningen.command-test
  (:require [clojure.test :refer :all]
            [leiningen.command :as c]))

(defn- successful-shell-fnc [& args]
  {:exit 0 :out ""})

(defn- failed-shell-fnc [& args]
  {:exit 128 :out "failed"})

(deftest ^:unit commit-successfully
  (testing "commit successfully with a not-empty message"
    (let [msg "[id-123] [sw] implement"]
      (is (= "Commited with the message: [id-123] [sw] implement"
             (c/commit-with msg successful-shell-fnc))))))

(deftest ^:unit commit-failed
  (testing "commit failed because message is empty"
    (let [msg ""]
      (is (= "Commit message is not correct"
             (c/commit-with msg successful-shell-fnc))))))

(deftest ^:unit commit-failed-msg
  (testing "commit failed with msg"
    (let [msg "msg"]
      (is (= "Commit has been rejected with the reason: failed"
             (c/commit-with msg failed-shell-fnc))))))