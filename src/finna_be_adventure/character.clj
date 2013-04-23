(ns finna-be-adventure.character
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c]))

(defn ^{:private true}
  ->character
  [c]
  c)

(def ^{:private true}
  newline
  (p/lift (constantly \newline)
          (prim/string "newline")))

(def ^{:private true}
  space
  (p/lift (constantly \space)
          (prim/string "space")))

(def ^{:private true}
  tab
  (p/lift (constantly \tab)
          (prim/string "tab")))

(def ^{:private true}
  return
  (p/lift (constantly \return)
          (prim/string "return")))

(def character-parser
  (c/label (p/lift (fn [_ c]
                     (->character c))
                   (prim/char \\)
                   (p/choose newline
                             space
                             tab
                             return
                             prim/any-char))
           "character"))