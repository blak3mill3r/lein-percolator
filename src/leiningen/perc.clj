(ns leiningen.perc
  (:use [leiningen.core.eval :only [eval-in-project]])
  (:use [clojure.pprint :only [pprint]])
  (:use [ns-tracker.core])
  (:import java.util.Date))

; FIXME split project perc dependencies into a separate project?
;
; so as to inject only one dependency

(defn add-ns-tracker-dep [project]
  "add ns-tracker to the project's classpath for dev-build loop"
  (if (some #(= 'ns-tracker (first %)) (:dependencies project))
    project
    (update-in project [:dependencies] conj ['ns-tracker "0.2.1"])))

(defn add-clj-stacktrace-dep [project]
  "add ns-tracker to the project's classpath for dev-build loop"
  (if (some #(= 'clj-stacktrace (first %)) (:dependencies project))
    project
    (update-in project [:dependencies] conj ['clj-stacktrace "0.2.5"])))

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
  (do
    (println "devbuildloop sourcedirs\n" ( concat source-dirs ["/home/blake/w/percolator/src"]))
    `(let [modified-namespaces# (ns-tracker [~@( concat source-dirs ["/home/blake/w/percolator/src"])])]
     (while true
       (do
       (Thread/sleep 100)
       (doseq [ns-sym# (modified-namespaces#)]
         (try
           (println "Brewing ... ")
           (let [starttime# (.getTime (Date.))]
             ; load clojure code
             (require ns-sym# :reload)
             ; brew with percolator
             ( percolator.core/write-all-cus-to-path ns-sym# ~(:source-path project))
             (println (format "shit was brewed (took %d ms)."
                              (- (.getTime (Date.)) starttime#))))
           ; catch exceptions and spit out stack trace for now, would be nice to have
           ; better percolator error messages in here, too
           (catch Throwable e#
             (println "OH NOES BADNESS HAPPEND :(")
             (clj-stacktrace.repl/pst+ e# ))))))) ))

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
        (add-ns-tracker-dep)
        (add-clj-stacktrace-dep))
      (dev-build-loop project [(:source-path project)] [] args)
      `(require 'ns-tracker.core 'percolator.core 'clj-stacktrace.repl))))

(defn perc [project & args]
  (if (some #{"dev"} args)
    (dev-build project args)
    (full-build project args)))
