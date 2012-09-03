(ns leiningen.perc
  ;(:require [clojure.java.classpath :as classpath])
  (:use [leiningen.compile :only [eval-in-project]])
  (:use [clojure.pprint :only [pprint]])
  (:use [ns-tracker.core]))

; build all percolator compilation units in the given namespace
(defn- build-namespace [project cu-ns]
  (eval-in-project
    project
    `(percolator.core/write-all-cus-to-path '~cu-ns ~(:source-path project))
    nil
    nil
    `(require 'percolator.core '~cu-ns)))

; build all units in all configured percolator namespaces
(defn- full-build [project & args]
  (doseq
  [ perc-ns ( :percolator-namespaces project ) ] ( build-namespace project perc-ns )))

(defn- dev-build-loop [project source-dirs opts args]
  `(let [modified-namespaces# (ns-tracker [~@source-dirs])]
     (while true
       (do
       (Thread/sleep 100)
       (doseq [ns-sym# (modified-namespaces#)]
         ; reload the modified clojure source
         (require ns-sym# :reload)
         ; build all the percolator compilation units in that namespace
         ( percolator.core/write-all-cus-to-path ns-sym# ~(:source-path project)))))))

; for development cycle
; watch for changes and rebuild percolator namespaces as their source is modified
(defn- dev-build [project & args]
  (do
    (eval-in-project
      project
      (dev-build-loop project [(:source-path project)] [] args)
      nil
      nil
      `(require 'ns-tracker.core 'percolator.core))))

(defn perc [project & args]
  (if (some #{"dev"} args)
    (dev-build project args)
    (full-build project args)))
