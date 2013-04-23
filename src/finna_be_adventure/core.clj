(ns finna-be-adventure.core
  (:refer-clojure :rename {cons core-cons
                           first core-first
                           rest core-rest
                           empty? core-empty?})
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c])
  (:use finna-be-adventure.boolean)
  (:use finna-be-adventure.nil)
  (:use finna-be-adventure.integer)
  (:use finna-be-adventure.float)
  (:use finna-be-adventure.character)
  (:use finna-be-adventure.symbol)
  (:use finna-be-adventure.string)
  (:use finna-be-adventure.list))

(def whitespace
  (p/choose prim/whitespace
            (prim/char \,)))

(def literal
  (p/choose (c/try float-parser)
            (c/try integer-parser)
            boolean-parser
            nil-parser
            character-parser
            string-parser
            symbol-parser))

(declare macro-table)

(def token
  (delay (p/lift (fn [d _] d)
                 (p/choose literal
                           (p/bind (prim/one-of (set (keys macro-table)))
                                   macro-table))
                 (c/many whitespace))))

(def macro-table
  {\( (list-parser token whitespace)})

(defn read [str]
  (p/parse str token))