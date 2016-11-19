(ns eu.icslab.gherega.alex.codo.main
    (:require [neko.activity :refer [defactivity set-content-view!]]
              [neko.ui :refer [make-ui]]
              [neko.action-bar :refer [setup-action-bar tab-listener]]
              [neko.debug :refer [*a]]
              ;[neko.notify :refer [toast]]
              [neko.resource :as res]
              [neko.find-view :refer [find-view]]
              [neko.threading :refer [on-ui]]
              [neko.dialog.alert :refer [alert-dialog-builder]]
              [eu.icslab.gherega.alex.codo.menu :as menu]
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

(defactivity eu.icslab.gherega.alex.codo.MainActivity
  :key :main

  (onCreate [this bundle]
    (.superOnCreate this bundle)
    (neko.debug/keep-screen-on this)
    (reset! utils/MA (*a))
    (on-ui
     (setup-action-bar this
                       {:title "Codo"
                        :icon R$drawable/logo
                        :display-options [:show-title :show-home]
                        :subtitle ""}))
    (on-ui
      (set-content-view! (*a)
        [:linear-layout {:orientation (do (json/write-str {:a 2}) :vertical)
                         :layout-width :match-parent
                         :layout-height :match-parent
                         :gravity :center-horizontal
                         :background-color (android.graphics.Color/parseColor "#ffffff")}


         (menu/add (*a))
         (todo/create (*a))
         (todo/create-add-button (*a))

         ;; TODO:
         ;; * logo :DONE:
         ;; * scroll view - :DONE:
         ;; * reset view - :DONE:
         ;; * click/long-click on checkboxes :DONE:
         ;;
         ;; * top-left menu for opening todos :DONE
         ;; * add :invisible-nil to status  to show it is visible and unchecked :DONE:
         ;; * load a todo and display it :DONE:
         ;; * time/date parser for timestamp manipulation :DONE:
         ;; * nicer theme

         ;; TODO: known bugs
         ;; * rotation resets activity and all becomes transparent
         ;; *** solution: also reste next atom - easy :DONE:
         ])
      (.setCompoundDrawablesWithIntrinsicBounds ^Button (find-view (*a) (keyword (str utils/ns-qualifier "menu"))) R$drawable/menuicon 0 0 0)
      (.setCompoundDrawablesWithIntrinsicBounds ^Button (find-view (*a) (keyword (str utils/ns-qualifier "addnew"))) 0 0 0 R$drawable/add))
    ))
