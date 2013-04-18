(ns finna-be-adventure.core
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s])
  (:require [finna-be-adventure.boolean :as b])
  (:require [finna-be-adventure.nil :as n])
  (:require [finna-be-adventure.integer :as i])
  (:require [finna-be-adventure.float :as f])
  (:require [finna-be-adventure.character :as c])
  (:require [finna-be-adventure.symbol :as sym])
  (:require [finna-be-adventure.string :as str])
  (:require [finna-be-adventure.list :as l]))

(def whitespace
  (p/choose s/whitespace
            (s/char \,)))

(declare macro-table)

(def number
  (p/choose (p/try f/float)
            i/integer))

(def token
  (p/lift (fn [d _] d)
          (p/choose number
                    b/boolean
                    n/nil
                    c/character
                    str/string
                    sym/symbol
                    (p/bind (s/one-of (set (keys macro-table)))
                            macro-table))
          (p/many whitespace)))

(def macro-table
  {\( (l/list token whitespace)})