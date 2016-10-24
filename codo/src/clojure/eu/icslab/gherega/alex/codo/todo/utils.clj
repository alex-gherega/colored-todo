(ns eu.icslab.gherega.alex.codo.todo.utils
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.action-bar :refer [setup-action-bar tab-listener]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.io :as io]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))

(def MAX-ITEMS (-> shapes/shapes
                   deref
                   count))

(def next (atom 1))

;; TODO: write some management API for the items-map
(def items-map (atom {}))

(defn load-todo [activity timestamp]
  (let [items-map (ser/read-json (io/read
                                  activity
                                  (str (:timestamp @items-map)
                                       ".txt")))]
    (apply assoc {:timestamp timestamp}
           (map-indexed #(array-map
                          (utils/make-id utils/item-prefix (inc %1))
                         %2)
                       (:items items-map)))))

(defn write-todo [activity]
  (io/write activity
            (str (:timestamp @items-map) ".txt")
            (-> @items-map ser/prepare ser/write-json)))

(defn update-item [id & {:keys [info status shape]
                                  :or {info (-> @items-map id :info)
                                       status (-> @items-map id :status)
                                       shape (-> @items-map id :shape)}}]
  (swap! items-map assoc id {:info info
                             :status status
                             :shape shape}))

(defn extract-item [id]
  (id @items-map))

(defn extract-field [id k]
  (-> id extract-item k))


(defn make-id [idx]
  (utils/make-id utils/item-prefix idx))

(defn clear-items-map [items-map]
  (swap! items-map assoc :timestamp (utils/get-timestamp))

  (doall (map #(update-item %
                            :info ""
                            :status nil
                            :shape (re-find #"border\d+" (extract-field % :shape)))
              (keys (dissoc @items-map :timestamp)))))

(defn set-drawable [activity id shape]
  (.setBackgroundDrawable ^TextView
                          (find-view activity id)
                          (ser/dig-shape shape)))

(defn reset-todo [activity]
  (write-todo activity)
  (reset! next 1)
  (clear-items-map items-map))
