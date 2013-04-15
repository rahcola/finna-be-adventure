(ns finna-be-adventure.integer
  (:require [finna-be-adventure.core :as c])
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s])
  (:import java.math.BigInteger))

(defn ->integer
  ([value-str]
     (->integer value-str 10))
  ([value-str radix]
     (BigInteger. value-str radix)))

(def digits-lower "0123456789abcdefghijklmnopqrstuvwxyz")
(def digits-upper "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")

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

(defn integer-in-radix [radix]
  (let [valid-digits (set (concat (take radix digits-lower)
                                  (take radix digits-upper)))]
    (p/lift (fn [sign digits]
              (->integer (reduce str sign digits) radix))
            (p/optional (s/one-of (set "+-")))
            (p/some (s/one-of valid-digits)))))

(def integer
  (c/token
   (p/choose (p/try (p/bind radix integer-in-radix))
             (integer-in-radix 10))))