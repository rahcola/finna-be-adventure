(ns finna-be-adventure.integer
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c])
  (:require [finna-be-adventure.expression.constant :as const])
  (:import java.math.BigInteger))

(defn ^{:private true}
  ->integer [value-str radix]
  (const/->Integer (BigInteger. value-str radix)))

(def ^{:private true}
  hex-radix
  (p/lift (constantly 16)
          (p/choose (prim/string "0x")
                    (prim/string "0X"))))

(defn ^{:private true}
  radix-i [i]
  (p/lift (constantly i)
          (prim/string (str i "r"))))

(def ^{:private true}
  radix
  (p/choose (c/try hex-radix)
            (apply p/choose (map radix-i (range 2 37)))))

(defn ^{:private true}
  digits [radix]
  (let [lower "0123456789abcdefghijklmnopqrstuvwxyz"
        upper "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"]
    (p/lift (partial reduce str)
            (c/some (prim/one-of (set (concat (take radix lower)
                                              (take radix upper))))))))

(defn ^{:private true}
  integer-in-radix [radix]
  (p/lift (fn [sign digits]
            (->integer (str sign digits) radix))
          (c/optional (prim/one-of (set "+-")))
          (digits radix)))

(def integer-parser
  (c/label (p/choose (c/try (p/bind radix integer-in-radix))
                     (integer-in-radix 10))
           "integer"))
