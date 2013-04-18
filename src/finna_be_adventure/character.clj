(ns finna-be-adventure.character
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(defn ->character
  [c]
  c)

(def newline
  (p/lift (constantly \newline)
          (s/string "newline")))

(def space
  (p/lift (constantly \space)
          (s/string "space")))

(def tab
  (p/lift (constantly \tab)
          (s/string "tab")))

(def return
  (p/lift (constantly \return)
          (s/string "return")))

(def character
  (p/label (p/lift (fn [_ c]
                     (->character c))
                   (s/char \\)
                   (p/choose newline
                             space
                             tab
                             return
                             s/any-char))
           "character"))