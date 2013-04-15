(ns finna-be-adventure.core
  (:refer-clojure :rename {list core-list})
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s])
  (:require [finna-be-adventure.integer :as i]))

(def whitespace
  (p/choose s/whitespace
            (s/char \,)))

(defn token [p]
  (p/lift (fn [x _] x) p (p/many whitespace)))

(def list
  (delay
   (token
    (p/lift (fn [_ forms _] (apply core-list forms))
            (s/char \()
            (p/many (p/choose i/integer
                              list))
            (s/char \))))))