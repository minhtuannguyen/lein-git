(defproject lein-git "0.1.0-SNAPSHOT"
  :description "lein-git is a leiningen-plugin which has been made in order to make commit whose messages follow certain pattern"
  :url "https://github.com/minhtuannguyen/lein-git"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :lein-git-spec [[:story-id :required] [:software-component :optional] [:commit-msg :required]]
  :eval-in-leiningen true)
