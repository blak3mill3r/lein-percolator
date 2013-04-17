(defproject lein-percolator "0.1.0-SNAPSHOT"
  :description "Percolator autobuilder plugin"
  :url "http://github.com/blak3mill3r/lein-percolator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure          "1.5.1"]
                 [org.clojure/java.classpath   "0.2.0"]
                 [percolator/percolator        "1.0.0-SNAPSHOT"]
                 [me.raynes/fs                 "1.4.0" ]
                 [ns-tracker                   "0.2.1" ]
                 [clj-stacktrace               "0.2.5" ]
                 ]
  :eval-in-leiningen true)
