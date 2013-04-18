(ns finna-be-adventure.list
  (:refer-clojure :rename {list core-list
                           cons core-cons
                           first core-first
                           rest core-rest
                           empty? core-empty?})
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(defprotocol Seq
  (first [s])
  (rest [s])
  (cons [s o])
  (empty? [s]))

(defn list->str [list]
  (loop [sb (StringBuilder. "(")
         list list]
    (if (empty? list)
      (do (.setCharAt sb (dec (.length sb)) \))
          (.toString sb))
      (recur (doto sb
               (.append (str (first list)))
               (.append " "))
             (rest list)))))

(deftype AList [car cdr]
  Seq
  (first [_] car)
  (rest [_] cdr)
  (cons [self o]
    (AList. o self))
  (empty? [_] false)
  Object
  (toString [self]
    (list->str self)))

(deftype Nil []
  Seq
  (first [_] nil)
  (rest [self] self)
  (cons [self o]
    (AList. o self))
  (empty? [_] true)
  Object
  (toString [_]
    "()"))

(defn ->list [datums]
  (if (core-empty? datums)
    (Nil.)
    (cons (->list (core-rest datums))
          (core-first datums))))

(defn list [datum whitespace]
  (p/lift (fn [_ datums _]
            (->list datums))
          (p/many whitespace)
          (p/many datum)
          (s/char \))))