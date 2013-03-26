(ns finna-be-adventure.core
  (:require [derp-octo-cyril.parser :as p]))

(def symbol-special (p/one-of #{\* \+ \! \- \_ \?}))

(def symbol-char (p/choose (p/char \.) p/alphanumeric symbol-special))

(def symbol-first-char (p/choose p/letter symbol-special))

(def symbol-part (p/lift (partial reduce str)
                         symbol-first-char
                         (p/many symbol-char)))

(def symbol-namespace
  (p/lift str
          symbol-part
          (p/char \/)))

(def p-symbol
  (p/lift (comp symbol str)
          (p/optional (p/char \:))
          (p/optional symbol-namespace)
          symbol-part))

(def string-escape
  (let [escapes {\t \tab
                 \b \backspace
                 \n \newline
                 \r \return
                 \' \'
                 \" \"
                 \\ \\}]
    (p/label (p/lift (fn [_ c] (get escapes c))
                     (p/char \\)
                     (p/one-of (set (keys escapes))))
             "escape character")))

(def string-character
  (p/label (p/not-char \") "character"))

(def p-string
  (p/lift (fn [_ chars _] (reduce str "" chars))
          (p/label (p/char \") "opening quote")
          (p/many (p/choose string-escape
                            string-character))
          (p/label (p/char \") "closing quote")))

(def integer-radix
  (p/lift (fn [radix-digits _]
            (reduce str "" radix-digits))
          (p/some p/digit)
          (p/char \r)))

(def integer-literal
  (p/lift (fn [sign digits]
            (reduce str sign digits))
          (p/optional (p/choose (p/char \-)
                                (p/char \+)))
          (p/some p/digit)))

(def integer-hex
  (p/lift (fn [_ digits]
            (reduce str "0x" digits))
          (p/choose (p/string "0x")
                    (p/string "0X"))
          (p/some (p/one-of (set (concat "0123456789"
                                         "abcdef"
                                         "ABCDEF"))))))

(def p-integer
  (p/choose (p/try (p/lift (fn [radix-as-str i-as-str]
                             (Integer/parseInt i-as-str
                                               (Integer/parseInt radix-as-str)))
                           integer-radix
                           integer-literal))
            (p/try (p/lift (fn [digits]
                             (Integer/decode digits))
                           integer-hex))
            (p/lift (fn [i-as-str]
                      (Integer/parseInt i-as-str))
                    integer-literal)))