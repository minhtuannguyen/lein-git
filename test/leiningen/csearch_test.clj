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

(deftest ^:unit search-test
  (testing "search with a key"
    (let [spec :story-id
          db [{:commit "commit 1" :message {:story-id           "[JIRA-123]",
                                            :software-component "[order]",
                                            :commit-msg         "[msg 1]"}}
              {:commit "commit 2" :message {:story-id           "[JIRA-678]",
                                            :software-component "[search]",
                                            :commit-msg         "[msg 2]"}}]
          query "JIRA-123"
          results (s/search db query spec)]
      (is (= 1 (count results)))
      (is (= "commit 1" (:commit (first results))))))

  (testing "search with another key"
    (let [spec :software-component
          db [{:commit "commit 1" :message {:story-id           "[JIRA-123]",
                                            :software-component "[order]",
                                            :commit-msg         "[msg 1]"}}
              {:commit "commit 2" :message {:story-id           "[JIRA-678]",
                                            :software-component "[search]",
                                            :commit-msg         "[msg 2]"}}]
          query "search"
          results (s/search db query spec)]
      (is (= 1 (count results)))
      (is (= "commit 2" (:commit (first results)))))))

(deftest ^:unit pp-fnc-test
  (testing "pp test"
    (let [results [{:commit "commit 1" :message {:story-id           "[JIRA-123]",
                                                 :software-component "[order]",
                                                 :commit-msg         "[msg 1]"}}

                   {:commit "commit 2" :message {:story-id           "[JIRA-678]",
                                                 :software-component "[search]",
                                                 :commit-msg         "[msg 2]"}}]]

      (is (= [{:commit             "commit 1"
               :story-id           "[JIRA-123]",
               :software-component "[order]",
               :commit-msg         "[msg 1]"}

              {:commit             "commit 2"
               :story-id           "[JIRA-678]",
               :software-component "[search]",
               :commit-msg         "[msg 2]"}]

             (s/pp-fnc results)))))

  (testing "pp-entry test"
    (let [results {:commit "commit 1" :message {:story-id           "[JIRA-123]",
                                                :software-component "[order]",
                                                :commit-msg         "[msg 1]"}}]

      (is (= {:commit             "commit 1"
              :story-id           "[JIRA-123]",
              :software-component "[order]",
              :commit-msg         "[msg 1]"}

             (s/pp-entry-fnc results))))))
