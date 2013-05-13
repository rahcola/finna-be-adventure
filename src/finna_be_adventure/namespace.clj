(ns finna-be-adventure.namespace)

(defprotocol Environment
  (set-name! [env symbol object])
  (get-name [env symbol]))

(deftype AFrame [^{:volatile-mutable true} table
                 parent]
  Environment
  (set-name! [_ symbol object]
    (set! table (assoc table symbol object)))
  (get-name [_ symbol]
    (get table symbol
         (if parent
           (get-name parent symbol)))))

(deftype ANamespace [name global-env]
  Environment
  (set-name! [_ symbol value]
    (set-name! global-env symbol value))
  (get-name [_ symbol]
    (get-name global-env symbol)))