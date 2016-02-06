(ns leiningen.csearch-test
  (:require [clojure.test :refer :all]
            [leiningen.csearch :as s]))

(defn git-log-fnc []
  [{:commit  "12345",
    :message "[ [id-123] [sw] [implement] ]"}])

(deftest ^:unit parse-log-to-structure-data
  (testing "parse logs to structure data"
    (let [spec [[:story-id :required] [:software-component :required]]
          logs (s/get-structured-logs spec git-log-fnc)]
      (is (= {:commit  "12345",
              :message {:story-id           "[id-123]",
                        :software-component "[sw]",
                        :commit-msg         "[implement]"}}
             (first logs))))))
