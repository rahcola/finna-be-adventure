(ns finna-be-adventure.character
  (:refer-clojure :rename {nil core-nil})
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(def ->nil core-nil)

(def nil
  (p/label (p/lift (fn [_]
                     ->nil)
                   (s/string "nil"))
           "nil"))