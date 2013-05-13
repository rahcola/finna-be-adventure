(ns finna-be-adventure.list
  (:require [finna-be-adventure.sequence :as seq])
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c]))

(defn ^{:private true}
  list->str [list]
  (loop [sb (StringBuilder. "(")
         list list]
    (if (seq/empty? list)
      (do (.setCharAt sb (dec (.length sb)) \))
          (.toString sb))
      (recur (doto sb
               (.append (str (seq/first list)))
               (.append " "))
             (seq/rest list)))))

(deftype AList [car cdr]
  seq/Sequence
  (first [_] car)
  (rest [_] cdr)
  (cons' [self o]
    (AList. o self))
  (empty? [_] false)
  Object
  (toString [self]
    (list->str self)))

(deftype Nil []
  seq/Sequence
  (first [_] nil)
  (rest [self] self)
  (cons' [self o]
    (AList. o self))
  (empty? [_] true)
  Object
  (toString [_]
    "()"))

(def ^{:private true}
  canonical-nil (Nil.))

(defn ^{:private true}
  ->list [datums]
  (if (empty? datums)
    canonical-nil
    (seq/cons (first datums)
              (->list (rest datums)))))

(defn list-parser [datum whitespace]
  (p/lift (fn [_ datums _]
            (->list datums))
          (c/many whitespace)
          (c/many datum)
          (prim/char \))))