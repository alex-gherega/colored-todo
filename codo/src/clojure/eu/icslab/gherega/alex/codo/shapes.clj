(ns eu.icslab.gherega.alex.codo.shapes
  (:require [neko.debug :refer [*a]]
            ;[neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.widget.PopupMenu
           android.text.Html
           android.os.Environment
           java.io.File))

(def colors (atom [(map-indexed
                    #(str %2 (inc %1) ".xml")
                    (repeat 8 "border"))]))

;; replace with nice macro
(def shapes (atom ["border3"
                   "border5"
                   "border7"
                   "border2"
                   "border4"
                   "border6"
                   "border8"
                   "border1"]))

(defn- update-shape [shapes]
  (let [shape (first shapes)
        shapes (-> shapes rest vec)]
    (conj shapes shape)))


(defn shape [shapes]
  (let [shape (first @shapes)
        _ (swap! shapes update-shape)]
    (println "Calling shape")
    shape))
