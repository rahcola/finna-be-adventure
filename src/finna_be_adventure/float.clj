(ns finna-be-adventure.float
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s])
  (:import java.math.BigDecimal))

(defn ->float [s]
  (BigDecimal. s))

(def point
  (s/char \.))

(def sign
  (s/one-of (set "+-")))

(def digits
  (p/lift (partial reduce str)
          (p/some (s/one-of (set "0123456789")))))

(def exponent
  (p/lift str
          (s/one-of (set "eE"))
          (p/optional sign)
          digits))

(def float
  (p/label (p/choose (p/lift (comp ->float str)
                             (p/optional sign)
                             digits
                             point
                             (p/optional digits)
                             (p/optional exponent))
                     (p/lift (comp ->float str)
                             (p/optional sign)
                             point
                             digits
                             (p/optional exponent))
                     (p/lift (comp ->float str)
                             (p/optional sign)
                             digits
                             exponent))
           "float"))