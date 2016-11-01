(ns eu.icslab.gherega.alex.codo.todo.ui
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.ui :refer [make-ui]]
            [neko.-utils :refer [app-package-name]]
            [neko.action-bar :refer [setup-action-bar tab-listener]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            [eu.icslab.gherega.alex.codo.todo.callbacks :as callbacks]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.view.View
           android.widget.Button
           android.text.Html))

(res/import-all)

(defn make-editbox [activity text]
  (make-ui activity
           [:edit-text
            {:text text
             :layout-width :match-parent
             :layout-height :wrap}]))

(defn make-dialog
  ([activity message]
   (alert-dialog-builder activity
                         {:message message
                          :cancelable true}))

  ([activity message callback]
   (alert-dialog-builder activity
                         {:message message
                          :cancelable true
                          :positive-text "OK"
                          :positive-callback callback}))

  ([activity id message edit-text callback]
   (make-dialog activity message
                (fn [dialog res]
                  (callback activity id edit-text)))))

(defmacro make-checkitem [activity id background]
  `[:text-view {:text ""
                :text-color (android.graphics.Color/parseColor "#ff0000")
                :text-size 20
                :padding-left 100 :padding-right 100 :padding-top 20 :padding-bottom 20
                :layout-margin 20
                :clickable true
                :visibility View/INVISIBLE
                :id ~id
                :background-drawable
                (res/get-drawable ~(symbol
                                    (str (app-package-name) ".R$drawable/" background))
                                  )
                :on-click (partial listeners/todo-onclick
                                   (*a :main)
                                   ~id)
                :on-long-click (partial listeners/todo-on-longclick
                                        (*a :main)
                                        ~id)

                }])

(defn make-todosv [items]
  [:scroll-view {:layout-width :match-parent
                 :layout-height 0;:wrap
                 :layout-weight 1
                 ;:fill-viewport false
                 }
   (into [:linear-layout {:orientation :vertical
                          :layout-width :match-parent
                          :layout-height :match-parent
                          ;:layout-height 0
                          ;:layout-weight 1
                          :gravity :center-horizontal
                          :background-color (android.graphics.Color/parseColor "#ffffff")
                                        ;:padding [50 50 50 50]
                          }

          ;; insert checkitems in it
          ;;(-> @tutils/items-map utils/order-todos)
          ]
         items)])

(defn make-add-button [onclick onlongclick]
  [:button {:id (keyword (str utils/ns-qualifier "addnew"))
            :layout-width :fill
            :layout-height :wrap
            :layout-gravity :bottom
            :layout-weight 0
            :text-color (android.graphics.Color/parseColor "#aaaaaa")
            :padding 50
            :background-color (android.graphics.Color/parseColor "#fefefe")
            :text ""
            ;; have used a plain string too.
            :on-click onclick
            :on-long-click onlongclick}])

;; @deprecated @unused
;; (defn update-item
;;   ([activity id item]
;;    ;; get current status & shape
;;    (let [;; change status & shape to next status & shape
;;          item (-> item ser/update-status ser/update-shape)
;;          shape (-> item :shape (.replace ".xml" ""))]
;;      (tutils/update-item id
;;                          :status (:status item)
;;                          :shape shape)
;;      ;; show it in ui
;;      (.setBackgroundDrawable ^TextView
;;                              (find-view activity id)
;;                              (->> shape
;;                                   (symbol "R$drawable")
;;                                   eval
;;                                   res/get-drawable)))))

(defn reset-ui [activity]
  ;; make all items invisible again
  (doall (map #(do (tutils/set-drawable activity
                                        %
                                        (tutils/extract-field % :shape))
                   (.setVisibility ^TextView
                                   (find-view activity %)
                                   View/INVISIBLE))

              (keys (dissoc @tutils/items-map :timestamp))))
  ;; set shape to empty border
  )
