(defproject lein-percolator "0.0.1-SNAPSHOT"
  :description "A leiningen plugin for building .java files with percolator"
  :dependencies [[org.clojure/clojure          "1.4.0"]
                 [org.clojure/java.classpath   "0.2.0"]
                 [percolator/percolator        "1.0.0-SNAPSHOT"]
                 ]
            :eval-in-leiningen true)
