(defproject lein-git "0.1.0-SNAPSHOT"
  :description "lein-git is a leiningen-plugin which has been made in order to make commit whose messages follow certain pattern"
  :url "https://github.com/minhtuannguyen/lein-git"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/data.json "0.2.6"]]

  :lein-git-spec [[:story-id :required] [:software-component :optional]]
  :eval-in-leiningen true)
