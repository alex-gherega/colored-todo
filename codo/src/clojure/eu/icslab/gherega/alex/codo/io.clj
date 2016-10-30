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
  (str (.getPath (.getExternalFilesDir activity nil))
       "/todos/"
       fname))

(defn write
  "Save to a public file some info; we'll use this to save our todo(s)"
  [activity fname json]
  (let [message (get-todo-path activity fname)
        res (io/make-parents message)]
    (spit message
          json)
    (toast (str "Writing this todo to: " fname)
           :long)))

(defn read
  "Read a json like todo from a file"
  [activity fname]
  (let [message (get-todo-path activity fname)
        res (slurp message)
        ]
    (toast (str "Reading the todo from: " fname)
           :long)
    res))

(defn find-last
  [activity]
  "Get the path of the last seriliazed todo"
  (if-let [f (-> activity get-todo-path io/file file-seq second)]
    (.getName ^File f)))

(defn read-last
  "Read the last written todo"
  [activity]
  (-> activity find-last read))
