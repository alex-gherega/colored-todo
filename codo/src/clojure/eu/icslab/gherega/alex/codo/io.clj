(ns eu.icslab.gherega.alex.codo.io
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.action-bar :refer [setup-action-bar tab-listener]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            [eu.icslab.gherega.alex.codo.serialization :as ser]
            [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:import android.os.Environment
           java.io.File))

(defn get-todo-path [activity & [fname]]
  (let [path-444 (.getPath (.getExternalFilesDir activity nil))
        path-420 (str (Environment/getExternalStorageDirectory)
                  (.getPath (.getFilesDir (.getApplicationContext neko.App/instance))))]
    (str path-444
         "/todos/"
         fname)))

(defn write
  "Save to a public file some info; we'll use this to save our todo(s)"
  [activity fname json]
  (let [message (get-todo-path activity fname)
        res (io/make-parents message)]
    (spit message
          json)
    ;; (toast (str "Writing this todo to: " fname)
    ;;        :long)
    ))

(defn read
  "Read a json like todo from a file"
  [activity fname]
  (let [message (get-todo-path activity fname)
        res (slurp message)
        ]
    ;; uncomment for DEBUG
    ;; (toast (str "Reading the todo from: " fname)
    ;;        :long)
    res))

(defn find-timestamps [activity]
  (if-let [fs (seq (->> activity get-todo-path
                        io/file file-seq (drop 1)))]
    (lazy-seq (sort #(> (-> %1 .getName utils/get-timestamp)
                        (-> %2 .getName utils/get-timestamp))
                    fs))))

(defn find-last
  [activity]
  "Get the path of the last seriliazed todo"
  (if-let [fs (find-timestamps activity)]
    (-> fs first .getName)))

(defn read-last
  "Read the last written todo"
  [activity]
  (-> activity find-last read))
