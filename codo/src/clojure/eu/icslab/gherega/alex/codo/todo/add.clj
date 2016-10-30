(ns eu.icslab.gherega.alex.codo.todo.add
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
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))

;; add button ui and logic
(defn add
  ([activity]
   (add activity (tutils/make-id @tutils/next))
   (swap! tutils/next inc))

  ([activity id]
   (let [next-status (-> id (tutils/extract-field :status) ser/cond-status)]
     (tutils/update-item id :status next-status)
     (.setVisibility ^TextView (find-view activity id) View/VISIBLE))))
