(ns finna-be-adventure.function)

(deftype AFunction [arguments body])

(defn ->function [arguments body]
  (AFunction. arguments body))