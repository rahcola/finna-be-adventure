(ns finna-be-adventure.boolean
  (:refer-clojure :rename {boolean core-boolean})
  (:require [finna-be-adventure.core :as c])
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(def boolean
  (c/token
   (p/choose (s/string "true")
             (s/string "false"))))