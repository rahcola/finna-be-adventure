(ns finna-be-adventure.string
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c]))

(defn ^{:private true}
  ->string [chars]
  chars)

(def ^{:private true}
  string-escape
  (let [escapes {\t \tab
                 \b \backspace
                 \n \newline
                 \r \return
                 \' \'
                 \" \"
                 \\ \\}]
    (p/lift (fn [_ c] (get escapes c))
            (prim/char \\)
            (prim/one-of (set (keys escapes))))))

(def ^{:private true}
  string-character
  (prim/not-char \"))

(def string-parser
  (p/lift (fn [_ chars _]
            (->string (reduce str "" chars)))
          (prim/char \")
          (c/many (p/choose string-escape
                            string-character))
          (prim/char \")))