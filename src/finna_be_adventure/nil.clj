(ns finna-be-adventure.nil
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c]))

(def nil-parser
  (c/label (p/lift (fn [_] nil)
                   (prim/string "nil"))
           "nil"))