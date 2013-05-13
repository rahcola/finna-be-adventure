(ns finna-be-adventure.sequence
  (:refer-clojure :exclude [first rest cons empty?]))

(defprotocol Sequence
  (first [s])
  (rest [s])
  (cons' [s o])
  (empty? [s]))

(defn cons [o seq]
  (cons' seq o))

(def second (comp first rest))