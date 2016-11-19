(ns eu.icslab.gherega.alex.codo.todo.listeners
  (:require [neko.debug :refer [*a]]
            ; [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            [eu.icslab.gherega.alex.codo.todo.ui :as ui]
            [eu.icslab.gherega.alex.codo.todo.callbacks :as callbacks]
            [eu.icslab.gherega.alex.codo.shapes :as shapes])
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))

(defn on-click
  ([activity id text callback _]
   (if (<= @tutils/next tutils/MAX-ITEMS)
     (let [edit-text (ui/make-editbox activity text)]
       (->> edit-text
            (.setView (ui/make-dialog activity
                                      id
                                      "TODO details: "
                                      edit-text
                                      callback))
            .show)
       true)))

  ([activity text _]
   (on-click activity
             (tutils/make-id @tutils/next)
             text
             callbacks/add-callback
             nil)))

(defn on-long-click [activity _]
  (tutils/reset-todo activity)
  (ui/reset-ui activity)
  ;; unccoment for DEBUG
  ;(toast activity (str "wait a bit .." activity) :long)
  true)

(defn todo-onclick [activity id _]
  ;; uncomment for DEBUG
  ;; (toast (str  "shot click " id " "
  ;;              (tutils/extract-field id :shape)
  ;;              " "
  ;;              (-> id (tutils/extract-field :status) ser/cond-status)
  ;;              ) :long)
  (let [current-shape (tutils/extract-field id :shape)
        next-shape (ser/cond-shape current-shape)
        next-status (-> id (tutils/extract-field :status) ser/cond-status)]
    (tutils/update-item id :shape next-shape :status next-status)
    (tutils/set-drawable activity
                         id
                         next-shape)
    (tutils/write-todo activity)
    true))

(defn todo-on-longclick [activity id _]
  ;; unccoment for DEBUG
  ;; (toast (str  "long lcikc " id
  ;;              " " (tutils/extract-field id :info)) :long)

  (on-click activity
            id
            (tutils/extract-field id :info)
            callbacks/info-callback
            nil)
  true)
