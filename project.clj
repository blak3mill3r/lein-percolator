(defproject lein-percolator "0.0.1-SNAPSHOT"
  :description "Percolator Autobuilder Plugin"
  :url "http://github.com/blak3mill3r/lein-percolator"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :dependencies [[org.clojure/clojure          "1.4.0"]
                 [org.clojure/java.classpath   "0.2.0"]
                 [percolator/percolator        "1.0.0-SNAPSHOT"]
                 [fs                           "1.1.2" ]
                 [ns-tracker                   "0.1.2" ]
                 ]
  :eval-in-leiningen true)
