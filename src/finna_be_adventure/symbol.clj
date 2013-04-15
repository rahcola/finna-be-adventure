(ns finna-be-adventure.symbol
  (:refer-clojure :rename {symbol core-symbol})
  (:require [finna-be-adventure.core :as c])
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(defn ->symbol [namespace symbol]
  {:namespace namespace
   :symbol symbol})

(def symbol-special
  (s/one-of (set "*+-!?_")))

(def symbol-character
  (p/choose (s/char \.)
            s/alphanumeric
            symbol-special))

(def symbol-first-character
  (p/choose s/letter
            symbol-special))

(def symbol-part
  (p/lift (partial reduce str)
          symbol-first-character
          (p/many symbol-character)))

(def namespace-part
  (p/lift (fn [s _] s)
          symbol-part
          (s/char \/)))

(def symbol
  (c/token
   (p/lift (fn [_ namespace symbol] (->symbol namespace symbol))
           (p/optional (s/char \:))
           (p/optional namespace-part)
           symbol-part)))