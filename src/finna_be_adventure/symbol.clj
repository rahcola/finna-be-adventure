(ns finna-be-adventure.symbol
  (:refer-clojure :rename {symbol core-symbol})
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(defn ->symbol
  ([symbol]
     (core-symbol symbol))
  ([namespace symbol]
     (core-symbol namespace symbol)))

(def symbol-special
  (s/one-of (set "*+-!?_")))

(def symbol-character
  (p/choose s/alphanumeric
            (p/lift str
                    (s/char \:)
                    (p/not-followed-by (s/char \:)))
            symbol-special))

(def symbol-first-character
  (p/choose s/letter
            symbol-special))

(def symbol-part
  (p/lift (partial apply str)
          symbol-first-character
          (p/many symbol-character)))

(def dot
  (p/lift str
          (s/char \.)
          symbol-character))

(def namespace-part
  (p/lift (partial apply str)
          symbol-first-character
          (p/many (p/choose dot
                            symbol-character))))

(def symbol
  (p/choose (p/try (p/lift (fn [namespace _ symbol]
                             (->symbol namespace symbol))
                           namespace-part
                           (s/char \/)
                           symbol-part))
            (p/lift ->symbol
                    symbol-part)))