(ns finna-be-adventure.string
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(defn ->string [chars]
  chars)

(def string-escape
  (let [escapes {\t \tab
                 \b \backspace
                 \n \newline
                 \r \return
                 \' \'
                 \" \"
                 \\ \\}]
    (p/lift (fn [_ c] (get escapes c))
            (s/char \\)
            (s/one-of (set (keys escapes))))))

(def string-character
  (s/not-char \"))

(def string
  (p/lift (fn [_ chars _]
            (->string (reduce str "" chars)))
          (s/char \")
          (p/many (p/choose string-escape
                            string-character))
          (s/char \")))