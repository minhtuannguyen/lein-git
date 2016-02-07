(ns leiningen.csearch-test
  (:require [clojure.test :refer :all]
            [leiningen.csearch :as s]))

(deftest ^:unit parse-log-to-structure-data-only-required
  (testing "parse logs to structure data where all fields are required"
    (let [spec [[:story-id :required] [:software-component :required]]
          git-log-fnc (fn [] [{:commit  "12345",
                               :message "[ [id-123] [sw] [implement] ]"}])
          logs (s/structured-logs-of spec git-log-fnc)]
      (is (= {:commit  "12345",
              :message {:story-id           "[id-123]",
                        :software-component "[sw]",
                        :commit-msg         "[implement]"}}
             (first logs))))))

(deftest ^:unit parse-log-to-structure-data-only-optional
  (testing "parse logs to structure data where some fields are optional"
    (let [spec [[:story-id :required] [:software-component :required] [:developer :optional]]
          git-log-fnc (fn [] [{:commit  "54321",
                               :message "[ [id-321] [order] [implement add2basket] ]"}])
          logs (s/structured-logs-of spec git-log-fnc)]
      (is (= {:commit  "54321",
              :message {:story-id           "[id-321]",
                        :software-component "[order]",
                        :commit-msg         "[implement add2basket]"}}
             (first logs))))))
