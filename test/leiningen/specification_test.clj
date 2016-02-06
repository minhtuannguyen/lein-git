(ns leiningen.specification-test
  (:require [clojure.test :refer :all]
            [leiningen.specification :as s]))

(deftest ^:unit parse-spec
  (testing "parse spec 1"
    (let [spec [[:story-id :required]
                [:software-component :optional]
                [:commit-msg :required]]]

      (is (= [{:question "story-id" :validate-fn s/required-input}
              {:question "software-component" :validate-fn s/optinal-input}
              {:question "commit-msg" :validate-fn s/required-input}]
             (s/parse spec)))))

  (testing "parse spec 2"
    (let [spec [[:story-id :optional]
                [:software-component :optional]
                [:commit-msg :optional]]]

      (is (= [{:question "story-id" :validate-fn s/optinal-input}
              {:question "software-component" :validate-fn s/optinal-input}
              {:question "commit-msg" :validate-fn s/optinal-input}]
             (s/parse spec)))))

  (testing "parse spec 3"
    (let [spec [[:story-id :required]
                [:software-component :optional]
                [:commit-msg :blub]]]

      (is (= [{:question "story-id" :validate-fn s/required-input}
              {:question "software-component" :validate-fn s/optinal-input}
              {:question "commit-msg" :validate-fn s/optinal-input}]
             (s/parse spec))))))

