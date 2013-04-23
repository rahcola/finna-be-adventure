(ns finna-be-adventure.boolean
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c]))

(def boolean-parser
  (c/label (p/choose (p/lift (constantly true)
                             (prim/string "true"))
                     (p/lift (constantly false)
                             (prim/string "false")))
           "boolean"))