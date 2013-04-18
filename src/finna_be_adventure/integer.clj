(ns finna-be-adventure.integer
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s])
  (:import java.math.BigInteger))

(defn ->integer [value-str radix]
  (BigInteger. value-str radix))

(def hex-radix
  (p/lift (constantly 16)
          (p/choose (s/string "0x")
                    (s/string "0X"))))

(defn radix-i [i]
  (p/lift (constantly i)
          (s/string (str i "r"))))

(def radix
  (p/choose (p/try hex-radix)
            (apply p/choose (map radix-i (range 2 37)))))

(defn digits [radix]
  (let [lower "0123456789abcdefghijklmnopqrstuvwxyz"
        upper "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"]
    (p/lift (partial reduce str)
            (p/some (s/one-of (set (concat (take radix lower)
                                           (take radix upper))))))))

(defn integer-in-radix [radix]
  (p/lift (fn [sign digits]
            (->integer (str sign digits) radix))
          (p/optional (s/one-of (set "+-")))
          (digits radix)))

(def integer
  (p/label (p/choose (p/try (p/bind radix integer-in-radix))
                     (integer-in-radix 10))
           "integer"))