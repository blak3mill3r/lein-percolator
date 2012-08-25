(ns leiningen.perc
  ;(:require [clojure.java.classpath :as classpath])
  (:use [leiningen.compile :only [eval-in-project]]))

(defn- build-namespace [project cu-ns]
  (let [path "/home/blake/w/percolator/play/src"]  ; FIXME get path from lein project
    (eval-in-project project
    `(percolator.core/write-all-cus-to-path '~cu-ns ~path)
    nil
    nil
    `(require 'percolator.core '~cu-ns)) ))

(defn perc [project & args]
  (doseq
    [ perc-ns ( :percolator-namespaces project ) ] ( build-namespace project perc-ns )))
