(ns utils.core
  (:require [clojure.data.xml :as xml]))

;; no checkmark shape
(defn make-border [width color radius]

  [[:stroke {:android:width (str width "dp")
             :android:color color}]

   [:corners {:android:radius (str radius "dp")}]])

(defn make-bg [bgcolor border]
  (conj border
        [:solid {:android:color bgcolor}]))

(defn make-shape [width color radius bg]
  [[:item {:android:width "100dp"
           :android:height "60dp"}
    (into [:shape]
          (make-bg bg (make-border width color radius)))
    ]])

(defn shape-to-xml [shape]
  (->
   (into [:layer-list {:xmlns:android "http://schemas.android.com/apk/res/android"}]
         shape)
   xml/sexp-as-element
   xml/indent-str))
