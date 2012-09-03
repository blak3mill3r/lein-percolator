(ns leiningen.perc
  ;(:require [clojure.java.classpath :as classpath])
  (:use [leiningen.compile :only [eval-in-project]])
  (:use [clojure.pprint :only [pprint]])
  )

(defn- build-namespace [project cu-ns]
  (eval-in-project project
  `(percolator.core/write-all-cus-to-path '~cu-ns ~(:source-path project))
  nil
  nil
  `(require 'percolator.core '~cu-ns)))

(defn perc [project & args]
  (doseq
  [ perc-ns ( :percolator-namespaces project ) ] ( build-namespace project perc-ns )))
