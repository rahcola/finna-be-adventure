(ns finna-be-adventure.symbol
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c]))

(defn ^{:private true}
  ->symbol
  ([sym]
     (symbol sym))
  ([namespace sym]
     (symbol namespace sym)))

(def ^{:private true}
  symbol-special
  (prim/one-of (set "*+-!?_")))

(def ^{:private true}
  symbol-character
  (p/choose prim/alphanumeric
            (p/lift str
                    (prim/char \:)
                    (c/not-followed-by (prim/char \:)))
            symbol-special))

(def ^{:private true}
  symbol-first-character
  (p/choose prim/letter
            symbol-special))

(def ^{:private true}
  symbol-part
  (p/lift (partial apply str)
          symbol-first-character
          (c/many symbol-character)))

(def ^{:private true}
  dot
  (p/lift str
          (prim/char \.)
          symbol-character))

(def ^{:private true}
  namespace-part
  (p/lift (partial apply str)
          symbol-first-character
          (c/many (p/choose dot
                            symbol-character))))

(def symbol-parser
  (c/label (p/choose (c/try
                      (p/lift (fn [namespace _ symbol]
                                (->symbol namespace symbol))
                              namespace-part
                              (prim/char \/)
                              symbol-part))
                     (p/lift ->symbol
                             symbol-part))
           "symbol"))