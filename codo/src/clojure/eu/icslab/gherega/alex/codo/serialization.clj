(ns eu.icslab.gherega.alex.codo.serialization
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.action-bar :refer [setup-action-bar tab-listener]]
            [neko.-utils :refer [app-package-name]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.utils :refer [inil item-prefix make-id]]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.widget.Button
           android.text.Html))

(res/import-all)
;; use files to store one CoDO list

;; each file is a JSON structured file

;; {"timestamp" : 123456789
;;  "items" : [{"info" : "what this item is about"
;;              "status" : inil|null|false|true
;;              "shape" : "border3.xml"},
;;              {...}.
;;              {...},
   ;;            ...]}

;; write fns
(defn prepare [todos-map]
  {:timestamp (:timestamp todos-map)
   :items (reduce #(conj %1 ((make-id item-prefix
                                            %2)
                             todos-map))
                  []
                  (->> todos-map count (range 1)))})

(defn write-json

  ([items-map]
   (json/write-str items-map))

  ([items-map info status shape-xml] ;; this items-map the transformed todo/items-map for json serialization
   (let [items (conj (or (:items items-map) [])
                     {:info info
                      :status status
                      :shape shape-xml})]
     (json/write-str (assoc items-map :items items)))))

;; read fns
(defn read-json [jsn]
  (json/read-str jsn :key-fn keyword))

;; select item
(defn select [idx items-map]
  (nth (:items items-map) idx))

;; updater
(defn update-json [jsn key data]
  (let [m (json/read-json jsn)
        key (cond (some #{:info :status :shape} [key]) key
                  :default :other)]
    (assoc m key data)))

(defn cond-status [s]
  (condp = s
    inil nil
    nil true
    true false
    false nil))

(defn update-status [item]
  (assoc item :status (-> item :status cond-status)))

(defn cond-shape [s]
  (condp re-find s
    #"badcheck_" (.replace s "badcheck_" "")
    #"check_" (str "bad" s)
    #"border" (str "check_" s)))

(defn update-shape [item]
  (assoc item
         :shape
         (cond-shape (:shape item))))

(defn dig-shape [shape]
  (res/get-drawable (->> shape
                        (str (app-package-name) ".R$drawable/")
                        read-string
                        eval)))

;; (defmacro dig-shape [shape]
;;   (let [s# shape]
;;     `(res/get-drawable ~(symbol
;;                          (str (app-package-name) ".R$drawable/" s#))
;;                        )))
