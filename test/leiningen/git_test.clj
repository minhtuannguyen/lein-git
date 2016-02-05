(ns leiningen.git-test
  (:require [clojure.test :refer :all]
            [leiningen.git :as git]))

(deftest ^:unit generate-commit-message
  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw "sw"
          msg "implement"]
      (is (= "[id-123] [sw] implement"
             (git/commit-msg story-id sw msg))))))

(deftest ^:unit check-for-valid-user-input
  (testing "user must answer all question with the not-emptystring"
    (let [story-id "id-123"
          sw ""
          msg "implement"]
      (is (= ""
             (git/commit-msg story-id sw msg)))))
  (testing "user must answer all question with the not-emptystring"
    (let [story-id "id-123"
          sw "sw"
          msg ""]
      (is (= ""
             (git/commit-msg story-id sw msg)))))

  (testing "user must answer all question with the not-emptystring"
    (let [story-id ""
          sw "sw"
          msg "implement"]
      (is (= ""
             (git/commit-msg story-id sw msg))))))


