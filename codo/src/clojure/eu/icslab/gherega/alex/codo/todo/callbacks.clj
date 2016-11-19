(ns eu.icslab.gherega.alex.codo.todo.callbacks
  (:require [neko.debug :refer [*a]]
            ;[neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            [eu.icslab.gherega.alex.codo.todo.add :as add]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))

(defn info-callback [activity id edit-text]
  (let [info (.getText edit-text)]
    ;; update items-map data structure
    (tutils/update-item id :info info)

    ;; write to file
    (tutils/write-todo activity)))

(defn add-callback [activity id edit-text]
  ;; update ui
  (add/add activity)

  ;; serialize
  (info-callback activity
                 (or id (tutils/make-id @tutils/next))
                 edit-text))
