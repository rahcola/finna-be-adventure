(ns finna-be-adventure.float
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c])
  (:import java.math.BigDecimal))

(defn ^{:private true}
  ->float [s]
  (BigDecimal. s))

(def ^{:private true}
  point
  (prim/char \.))

(def ^{:private true}
  sign
  (prim/one-of (set "+-")))

(def ^{:private true}
  digits
  (p/lift (partial reduce str)
          (c/some (prim/one-of (set "0123456789")))))

(def ^{:private true}
  exponent
  (p/lift str
          (prim/one-of (set "eE"))
          (c/optional sign)
          digits))

(def float-parser
  (c/label (p/choose (p/lift (comp ->float str)
                             (c/optional sign)
                             digits
                             point
                             (c/optional digits)
                             (c/optional exponent))
                     (p/lift (comp ->float str)
                             (c/optional sign)
                             point
                             digits
                             (c/optional exponent))
                     (p/lift (comp ->float str)
                             (c/optional sign)
                             digits
                             exponent))
           "float"))