(ns leiningen.commit-test
  (:require [clojure.test :refer :all]
            [leiningen.commit :as c]))

(deftest ^:unit generate-commit-message
  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw "sw"
          msg "implement"
          message [story-id sw msg]]
      (is (= "[ [id-123] [sw] [implement] ]"
             (c/commit-msg message)))))

  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw ""
          msg "implement"
          message [story-id sw msg]]
      (is (= "[ [id-123] [implement] ]"
             (c/commit-msg message)))))
  (testing "generate commit string from the user input"
    (let [story-id "id-123"
          sw "sw"
          msg ""
          message [story-id sw msg]]
      (is (= "[ [id-123] [sw] ]"
             (c/commit-msg message)))))

  (testing "generate commit string from the user input"
    (let [story-id ""
          sw "sw"
          msg "implement"
          message [story-id sw msg]]
      (is (= "[ [sw] [implement] ]"
             (c/commit-msg message))))))

(deftest ^:unit parse-spec
  (testing "parse spec 1"
    (let [spec [[:story-id :required]
                [:software-component :optional]
                [:commit-msg :required]]]

      (is (= [{:question "story-id" :validate-fn c/required-input}
              {:question "software-component" :validate-fn c/optinal-input}
              {:question "commit-msg" :validate-fn c/required-input}]
             (c/parse spec)))))

  (testing "parse spec 2"
    (let [spec [[:story-id :optional]
                [:software-component :optional]
                [:commit-msg :optional]]]

      (is (= [{:question "story-id" :validate-fn c/optinal-input}
              {:question "software-component" :validate-fn c/optinal-input}
              {:question "commit-msg" :validate-fn c/optinal-input}]
             (c/parse spec)))))

  (testing "parse spec 3"
    (let [spec [[:story-id :required]
                [:software-component :optional]
                [:commit-msg :blub]]]

      (is (= [{:question "story-id" :validate-fn c/required-input}
              {:question "software-component" :validate-fn c/optinal-input}
              {:question "commit-msg" :validate-fn c/optinal-input}]
             (c/parse spec))))))
