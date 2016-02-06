(ns leiningen.commit-test
  (:require [clojure.test :refer :all]
            [leiningen.commit :as c]))

(deftest ^:unit generate-commit-message
  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw "sw"
          msg "implement"
          message [story-id sw]]
      (is (= "[ [id-123] [sw] [implement] ]"
             (c/commit-msg message msg)))))

  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw ""
          msg "implement"
          message [story-id sw]]
      (is (= "[ [id-123] [implement] ]"
             (c/commit-msg message msg)))))
  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw "sw"
          msg ""
          message [story-id sw]]
      (is (= "[ [id-123] [sw] [] ]"
             (c/commit-msg message msg)))))

  (testing "generate commit string from the user input"
    (let [story-id ""
          sw "sw"
          msg "implement"
          message [story-id sw]]
      (is (= "[ [sw] [implement] ]"
             (c/commit-msg message msg))))))