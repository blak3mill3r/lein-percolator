  

(ns leiningen.perc
  ;(:require [clojure.java.classpath :as classpath])
  (:use [leiningen.core.eval :only [eval-in-project]])
  (:use [clojure.pprint :only [pprint]])
  (:use [ns-tracker.core])
  (:import java.util.Date))

; build all percolator compilation units in the given namespace
(defn- build-namespace [project cu-ns]
  (eval-in-project
    project
    `(percolator.core/write-all-cus-to-path '~cu-ns ~(:source-path project))
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
         (try
           (print "Brewing ... ")
           (let [starttime# (.getTime (Date.))]
             ; load clojure code
             (require ns-sym# :reload)
             ; brew with percolator
             ( percolator.core/write-all-cus-to-path ns-sym# ~(:source-path project))
             (println (format "shit was brewed (took %d ms)."
                              (- (.getTime (Date.)) starttime#))))
           ; catch exceptions and spit out stack trace for now, would be nice to have
           ; percolator errors in there, too
           (catch Throwable e#
             (println "OH NOES BADNESS HAPPEND :(")
             (println e#)
             (comment (clj-stacktrace.repl/pst+ e#) ))))))))

(defn add-ns-tracker-dep [project]
  (if (some #(= 'ns-tracker (first %)) (:dependencies project))
    project
    (update-in project [:dependencies] conj ['ns-tracker "0.2.0"])))


; for development cycle
; watch for changes and rebuild percolator namespaces as their source is modified
(defn- dev-build [project & args]
  (do
    (eval-in-project
      (-> project
        (add-ns-tracker-dep))
      (dev-build-loop project [(:source-path project)] [] args)
      `(require 'ns-tracker.core 'percolator.core))))

(defn perc [project & args]
  (if (some #{"dev"} args)
    (dev-build project args)
    (full-build project args)))
