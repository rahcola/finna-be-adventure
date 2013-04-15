(ns finna-be-adventure.string
  (:require [finna-be-adventure.core :as c])
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.sequence-primitives :as s]))

(defn ->string [chars]
  (apply str chars))

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
  (c/token
   (p/lift (fn [_ chars _] (->string chars))
           (s/char \")
           (p/many (p/choose string-escape
                             string-character))
           (s/char \"))))