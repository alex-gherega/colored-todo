(ns eu.icslab.gherega.alex.codo.utils
   (:require [eu.icslab.gherega.alex.codo.time :refer [to-millis-from-epoch
                               date-time
                               formatter
                               print]]
   ;;[clojure.data.xml :as xml]
  ;;[neko.activity :refer [defactivity set-content-view!]]
  ;;           [neko.debug :refer [*a]]
  ;;           [neko.notify :refer [toast]]
  ;;           [neko.resource :as res]
  ;;           [neko.find-view :refer [find-view]]
  ;;          [neko.threading :refer [on-ui]]
            ;[clj-time.coerce :as time-coerce]
            ;[clj-time.core :as time-core]
            ;[clj-time.format :as time-format]
           )
  ;; (:import android.widget.EditText
  ;;          android.widget.TextView)
)

(def inil :invisible-nil)

(def item-prefix "todoitem")

(def ns-qualifier "eu.icslab.gherega.alex.codo.utils")

(def colors ["#ffd333"
             "#2534b9"
             "#32bd26"
             "#ff3333"
             "#d12a94"
             "#a0d32a"
             "#2ab1d0"
             "#ff8133"])

(def MA (atom nil))

(defn mkw [key]
  (->> key
      name
      (str ns-qualifier)
      keyword))

(defn make-id [prefix idx]
  (->> idx
       (str prefix)
       (keyword ns-qualifier)))

(defn get-timestamp
  ([]
   ;;123456789
   (to-millis-from-epoch (date-time)))
  ([str-ts]
   (read-string (.replace str-ts ".txt" "")))
)

(defn format-timestamp [ts]
  (let [joda-date (date-time ts)
        formatter (formatter "yyyy/MM/dd hh:mm")]
    (print formatter joda-date)))

(defn order-todos [todos-map]
  (reduce #(conj %1 ((make-id item-prefix %2)
                     todos-map))
          []
          (->> todos-map count dec (range 1))))

(defn make-border [width color radius]

  [[:stroke {:android:width (str width "dp")
             :android:color color}]

   [:corners {:android:radius (str radius "dp")}]])

(defn make-shape [bgcolor border]
  (conj border
        [:solid {:android:color bgcolor}]))

;; (defn shape-to-xml [width color radius bg]
;;   (->
;;    (into [:shape {:xmlns:android "http://schemas.android.com/apk/res/android"}]
;;          (make-shape bg (make-border width color radius)))
;;    xml/sexp-as-element
;;    xml/indent-str))

;; (defn write-shapes [f-name]
;;   (map-indexed #(spit (str f-name (inc %1) ".xml")
;;                     (shape-to-xml 4 %2 8 "#ffffff"))
;;              colors))
