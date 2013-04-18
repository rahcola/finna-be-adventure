(ns finna-be-adventure.boolean
  (:refer-clojure :rename {boolean core-boolean})
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(def boolean
  (p/label (p/choose (p/lift (constantly true)
                             (s/string "true"))
                     (p/lift (constantly false)
                             (s/string "false")))
           "boolean"))