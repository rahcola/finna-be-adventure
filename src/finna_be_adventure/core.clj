(ns finna-be-adventure.core
  (:require [derp-octo-cyril.parser :as p])
  (:require [derp-octo-cyril.primitives :as prim])
  (:require [derp-octo-cyril.combinators :as c])
  (:use finna-be-adventure.boolean)
  (:use finna-be-adventure.nil)
  (:use finna-be-adventure.integer)
  (:use finna-be-adventure.float)
  (:use finna-be-adventure.character)
  (:use finna-be-adventure.symbol)
  (:use finna-be-adventure.string)
  (:use finna-be-adventure.list))

(def whitespace
  (p/choose prim/whitespace
            (prim/char \,)))

(def literal
  (p/choose (c/try float-parser)
            (c/try integer-parser)
            boolean-parser
            nil-parser
            character-parser
            string-parser
            symbol-parser))

(declare macro-table)

(def token
  (delay (p/lift (fn [d _] d)
                 (p/choose literal
                           (p/bind (prim/one-of (set (keys macro-table)))
                                   macro-table))
                 (c/many whitespace))))

(def macro-table
  {\( (list-parser token whitespace)})

(defn read [str]
  (p/parse str token))

(def class-data
  "<CA><FE><BA><BE>^@^@^@3^@ESC
^@^E^@^N        ^@^O^@^P
^@^Q^@^R^G^@^S^G^@^T^A^@^F<init>^A^@^C()V^A^@^DCode^A^@^OLineNumberTable^A^@^Dmain^A^@^V([Ljava/lang/String;)V^A^@
SourceFile^A^@  Test.java^L^@^F^@^G^G^@^U^L^@^V^@^W^G^@^X^L^@^Y^@^Z^A^@^DTest^A^@^Pjava/lang/Object^A^@^Pjava/lang/System^A^@^Cout^A^@^ULjava/io/PrintStream;^A^@^Sjava/io/PrintStream^A^@^Gprintln^A^@^D(I)V^@!^@^D^@^E^@^@^@^@^@^B^@^A^@^F^@^G^@^A^@^H^@^@^@^]^@^A^@^A^@^@^@^E*<B7>^@^A<B1>^@^@^@^A^@ ^@^@^@^F^@^A^@^@^@^A^@  ^@
^@^K^@^A^@^H^@^@^@&^@^B^@^A^@^@^@
<B2>^@^B^Q^E4<B6>^@^C<B1>^@^@^@^A^@     ^@^@^@
^@^B^@^@^@^C^@  ^@^D^@^A^@^L^@^@^@^B^@
")