(ns leiningen.commit-test
  (:require [clojure.test :refer :all]
            [leiningen.commit :as c]))

(deftest ^:unit generate-commit-message
  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw "sw"
          msg "implement"
          message [story-id sw msg]]
      (is (= "[[id-123] [sw] [implement]]"
             (c/commit-msg message))))))

(deftest ^:unit check-for-valid-user-input
  (testing "user must answer all question with the not-emptystring"
    (let [story-id "id-123"
          sw ""
          msg "implement"
          message [story-id sw msg]]
      (is (= ""
             (c/commit-msg message)))))
  (testing "user must answer all question with the not-emptystring"
    (let [story-id "id-123"
          sw "sw"
          msg ""
          message [story-id sw msg]]
      (is (= ""
             (c/commit-msg message)))))

  (testing "user must answer all question with the not-emptystring"
    (let [story-id ""
          sw "sw"
          msg "implement"
          message [story-id sw msg]]
      (is (= ""
             (c/commit-msg message))))))
