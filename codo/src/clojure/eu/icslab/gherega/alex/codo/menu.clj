(ns eu.icslab.gherega.alex.codo.menu
  (:require [neko.activity :refer [set-content-view!]]
            [neko.ui :refer [make-ui]]
            [neko.debug :refer [*a]]
            ;[neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.io :as io]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            [eu.icslab.gherega.alex.codo.todo :as todo]
            [eu.icslab.gherega.alex.codo.todo.ui :as ui]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.widget.Button
           android.text.Html))

(res/import-all)

(def menu-view (atom nil))
(declare add)

(defn tv-entry-click [activity ts]
  (fn [_]
    (tutils/reset-todo activity)
    (ui/reset-ui activity)

    (on-ui

      (set-content-view! activity
        [:linear-layout {:orientation (do (json/write-str {:a 2}) :vertical)
                         :layout-width :match-parent
                         :layout-height :match-parent
                         :gravity :center-horizontal
                         :background-color (android.graphics.Color/parseColor "#ffffff")
                         ;:padding [50 50 50 50]
                         }


         (add activity)
         (ui/make-todosv (todo/init activity ts))
         (todo/create-add-button activity)

         ;; TODO:
         ;; * logo :DONE:
         ;; * scroll view - :DONE:
         ;; * reset view - :DONE:
         ;; * click/long-click on checkboxes :DONE:
         ;;
         ;; * top-left menu for opening todos :DONE:
         ;; * add :invisible-nil to status  to show it is visible and unchecked :DONE:
         ;; * load a todo and display it :DONE:
         ;; * time/date parser for timestamp manipulation :DONE:
         ;; * nicer theme

         ;; TODO: known bugs
         ;; * rotation resets activity and all becomes transparent
         ;; *** solution: also reste next atom - easy
         ])
      (.setCompoundDrawablesWithIntrinsicBounds ^Button (find-view activity (keyword (str utils/ns-qualifier "menu"))) R$drawable/menuicon 0 0 0)
      (.setCompoundDrawablesWithIntrinsicBounds ^Button (find-view activity (keyword (str utils/ns-qualifier "addnew"))) 0 0 0 R$drawable/add))
    (-> menu-view deref .dismiss)
    (reset! menu-view nil)))

(defn make-tv-entry [activity fname]
  (let [ts (utils/get-timestamp fname)
        ts-format (utils/format-timestamp ts)]
    [:text-view {:text ts-format
                 :id ts
                 :padding [50 50 50 50]
                 :background-color (android.graphics.Color/parseColor "#ffffff")
                 :text-color (android.graphics.Color/parseColor "#afafaf")
                 :on-click (tv-entry-click activity ts)}]))

(defn make-tves-layout [activity]
  (make-ui activity
           [:linear-layout {:orientation (do (json/write-str {:a 2}) :vertical)
                            :layout-width :match-parent
                            :layout-height :match-parent
                            :gravity :center-horizontal
                            :background-color (android.graphics.Color/parseColor "#ffffff")
                            ;:padding [50 50 50 50]
                            }

            [:scroll-view {:layout-width :match-parent
                           :layout-height 0;:wrap
                           :layout-weight 1
                           :fill-viewport false
                           }

             (into [:linear-layout {:orientation :vertical
                                    :layout-width :match-parent
                                    :layout-height :match-parent
                                    :gravity :center-horizontal
                                    ;:padding [50 50 50 50]
                                    :background-color (android.graphics.Color/parseColor "#ffffff")}]
                   (reduce #(conj %1
                                  (make-tv-entry activity
                                                 (.getName %2)))
                           []
                           (io/find-timestamps activity)))]]))

(defn make-tves [activity]
  (let [builder (ui/make-dialog activity
                                "load a TODO: ")
        view (->> (make-tves-layout activity)
                  (.setView builder)
                  .show)]
    (reset! menu-view view)))

(defn add [activity]
  [:button {:id (keyword (str utils/ns-qualifier "menu"))
            :layout-width :fill
            :layout-height :wrap
            :padding 50
            :text ""
                                        ;:background-drawable (res/get-drawable R$drawable/border1)
            :background-color (android.graphics.Color/parseColor "#f9f9f9")
            ;; have used a plain string too.
            :on-click (fn [_] (make-tves activity))}])
