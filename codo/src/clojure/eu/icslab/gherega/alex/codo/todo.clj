(ns eu.icslab.gherega.alex.codo.todo
  (:require [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.dialog.alert :refer [alert-dialog-builder]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [eu.icslab.gherega.alex.codo.io :as io]
            [eu.icslab.gherega.alex.codo.shapes :as shapes]
            [eu.icslab.gherega.alex.codo.utils :refer [get-timestamp inil item-prefix make-id]]
            [eu.icslab.gherega.alex.codo.todo.utils :as tutils]
            [eu.icslab.gherega.alex.codo.todo.listeners :as listeners]
            [eu.icslab.gherega.alex.codo.todo.ui :as ui]
            [eu.icslab.gherega.alex.codo.todo.loader :as loader]
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
  (let [id# #(make-id item-prefix %)
        bg# (shapes/shape shapes/shapes)]

    `(do (tutils/update-item (~id# ~idx) :info "" :status inil :shape ~bg#)

         (ui/make-checkitem ~activity (~id# ~idx) ~bg#))))

(defmacro create-checkitems [activity]
  (reduce (fn [x# y#] `(->> ~y# (create-tv ~activity) (conj ~x#)))
          []
          (range 1 (inc tutils/MAX-ITEMS))))


(defn init [activity]
  (let [td (loader/load-todo activity)]
    (toast activity (str td) :long))

  (reset! tutils/items-map
          (loader/load-todo activity)
          ;{:timestamp (get-timestamp)}
          )

  ;; TODO: now load the last todo-file saved: make an ordered list o timestamps and pick last
  (if (tutils/has-loaded-todo?)
    (reset! tutils/next (tutils/infer-next))
    (reset! tutils/next 1))

  (if (tutils/has-loaded-todo?)
    (loader/create-checkitems activity @tutils/items-map) ;; TODO:
    (create-checkitems activity))
  )

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
