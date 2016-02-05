(defproject lein-git "0.1.0"
  :description "lein-git is a leiningen-plugin which has been made in order to make commit whose messages follow certain pattern"
  :url "https://github.com/minhtuannguyen/lein-git"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :lein-git [[:story-id] [:software] [:commit-msg]]
  :eval-in-leiningen true)
