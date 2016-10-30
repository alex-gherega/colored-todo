(ns eu.icslab.gherega.alex.codo.todo.loader
  (:require [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.-utils :refer [app-package-name]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.string :refer [split]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.io :as io]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            [eu.icslab.gherega.alex.codo.todo.listeners :as listeners]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))

(res/import-all)

;; TODO: next 2 functions should be private
(defn get-last-ts [activity]
  (if-let [fname (io/find-last activity)]
    (-> fname (split #"/") last
        (.replace ".txt" "") read-string)))

(defn prepare [item]
  (if (= (:status item) (name utils/inil))
    (assoc item :status utils/inil)
    item))

(defn read-todo [activity timestamp]
  (let [input (io/read
               activity
               (str timestamp ".txt"))
        items-map (ser/read-json input)]
    (apply assoc {:timestamp timestamp}
           (reduce into (map-indexed
                         #(vector
                           (utils/make-id utils/item-prefix (inc %1))
                           (prepare %2))
                         (:items items-map))))
    ))

(defn load-todo
  ([activity]
   (if-let [timestamp (get-last-ts activity)]
     (load-todo activity timestamp)
     {:timestamp (utils/get-timestamp)}))
  ([activity timestamp]
   (read-todo activity timestamp)))

(defn make-checkitem [activity id status background]
  [:text-view {:text ""
               :text-color (android.graphics.Color/parseColor "#ff0000")
               :text-size 20
               :padding-left 100 :padding-right 100 :padding-top 20 :padding-bottom 20
               :layout-margin 20
               :clickable true
               :visibility (tutils/get-visibility status)
               :id id
               :background-drawable
               (res/get-drawable (-> (str (app-package-name) ".R$drawable/" background)
                                     read-string
                                     eval))
               :on-click (partial listeners/todo-onclick
                                  (*a :main)
                                  id)
               :on-long-click (partial listeners/todo-on-longclick
                                       (*a :main)
                                       id)

               }])

(defn create-checkitems [activity items-map]
  (reduce (fn [items entry]
            (conj items (make-checkitem activity
                                        (key entry)
                                        (-> entry val :status)
                                        (-> entry val :shape))))
          []
          (dissoc items-map :timestamp)))
