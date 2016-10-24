(ns eu.icslab.gherega.alex.codo.main
    (:require [neko.activity :refer [defactivity set-content-view!]]
              [neko.ui :refer [make-ui]]
              [neko.action-bar :refer [setup-action-bar tab-listener]]
              [neko.debug :refer [*a]]
              [neko.notify :refer [toast]]
              [neko.resource :as res]
              [neko.find-view :refer [find-view]]
              [neko.threading :refer [on-ui]]
              [neko.dialog.alert :refer [alert-dialog-builder]]
              [eu.icslab.gherega.alex.codo.utils :as utils]
              [eu.icslab.gherega.alex.codo.serialization :as ser]
              [eu.icslab.gherega.alex.codo.todo :as todo]
              [eu.icslab.gherega.alex.codo.todo.ui :as ui]
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

;; We execute this function to import all subclasses of R class. This gives us
;; access to all application resources.
(res/import-all)

(defn save-to-file
  "Finds an EditText element with ID ::user-input in the given activity. Gets
  its contents and displays them in a toast if they aren't empty. We use
  resources declared in res/values/strings.xml."
  [activity]
  (let [ message (str (.getPath (.getExternalFilesDir (*a) nil)) "/CoDo/t.txt")
        res (io/make-parents message)]
    (toast (do
             (spit message
                   "ABECEDAR")
             message)
           :long)))

;; This is how an Activity is defined. We create one and specify its onCreate
;; method. Inside we create a user interface that consists of an edit and a
;; button. We also give set callback to the button.
(defactivity eu.icslab.gherega.alex.codo.MainActivity
  :key :main

  (onCreate [this bundle]
    (.superOnCreate this bundle)
    (neko.debug/keep-screen-on this)
    (reset! utils/MA (*a))
    (on-ui

      (set-content-view! (*a)
        [:linear-layout {:orientation (do (json/write-str {:a 2}) :vertical)
                         :layout-width :match-parent
                         :layout-height :match-parent
                         :gravity :center-horizontal
                         :background-color (android.graphics.Color/parseColor "#ffffff")
                         ;:padding [50 50 50 50]
                         }
         [:button {:id ::menu
                   :layout-width :fill
                   :layout-height :wrap
                   :padding 50
                   :text ""
                   :background-drawable (res/get-drawable R$drawable/border1)
                   :background-color (android.graphics.Color/parseColor "#f9f9f9")
                   ;; have used a plain string too.
                   :on-click (fn [_]
                               (.show (ui/make-dialog (*a)
                                                      "On next release:
\n 20/11/2016"
                                                      (fn [_ _] nil)))
                               ;; (let [pm (PopupMenu. (*a)
                               ;;                      (find-view (*a) ::menu))]
                               ;;   (.inflate (.getMenuInflater pm)
                               ;;             R$menu/popup_menu
                               ;;             (.getMenu pm))
                               ;;   (.show pm))
                               )}]

         (todo/create (*a))
         (todo/create-add-button (*a))

         ;; TODO:
         ;; * scroll view - :DONE:
         ;; * add :1 to status  to show it is visible and unchecked
         ;; * reset view - :DONE:
         ;; * click/long-click on checkboxes :DONE:
         ;; * top-left menu for opening todos :FOR-NEXT-RELease
         ;; * load a todo and display it
         ;; * time/date parser for timestamp manipulation
         ;; * nicer theme
         ;; * logo :DONE:
         ])
      (.setCompoundDrawablesWithIntrinsicBounds ^Button (find-view (*a) ::menu) R$drawable/menuicon 0 0 0)
      (.setCompoundDrawablesWithIntrinsicBounds ^Button (find-view (*a) (keyword (str utils/ns-qualifier "addnew"))) 0 0 0 R$drawable/add))
    ))
