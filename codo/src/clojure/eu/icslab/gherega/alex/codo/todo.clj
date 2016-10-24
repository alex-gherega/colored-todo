(ns eu.icslab.gherega.alex.codo.todo
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.action-bar :refer [setup-action-bar tab-listener]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.io :as io]
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            [eu.icslab.gherega.alex.codo.todo.listeners :as listeners]
            [eu.icslab.gherega.alex.codo.todo.ui :as ui]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))
;;
;; todo list
;;
(res/import-all)

(defmacro create-tv [activity idx]
  (let [id# #(utils/make-id utils/item-prefix %)
        bg# (shapes/shape shapes/shapes)]

    `(do (tutils/update-item (~id# ~idx) :info "" :status nil :shape ~bg#)

         (ui/make-checkitem ~activity (~id# ~idx) ~bg#))))

(defmacro create-checkitems [activity]
  (reduce (fn [x# y#] `(->> ~y# (create-tv ~activity) (conj ~x#)))
          []
          (range 1 (inc tutils/MAX-ITEMS))))

(defn init [activity]
  (reset! tutils/items-map
          {:timestamp (utils/get-timestamp)})
  (create-checkitems activity))

(defn create [activity]
  ;; make a linear-layout tree
  (ui/make-todosv (init activity)))

(defn show-info [activity item]
  ;(let [idx (-> id name last str read-string)])
  (-> (alert-dialog-builder activity
                            {:message (:info item)
                             :cancelable true
                             :positive-text "OK"
                            ;:positive-callback (fn [dialog res] ...)
                             })
      .create))

;;
;; add button
;;

(defn create-add-button [activity]
  (ui/make-add-button (partial listeners/on-click activity "")
                      (partial listeners/on-long-click activity)))
