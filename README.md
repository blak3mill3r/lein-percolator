# plugin

lein-percolator
This is a leiningen plugin to manage building .java files with percolator. It's
experimental.

## Usage

project.clj
```clojure
  (defproject myproject "1.2.3"
   .
   .
   .
   :percolator-options {}
   :percolator-builders [myproject.a-cu-namespace]
   .
   .
   .
  )
```

something like:
```bash
$ lein perc
```
maybe...

## License

Copyright (C) 2012 Blake Miller

Distributed under the Eclipse Public License, the same as Clojure.
