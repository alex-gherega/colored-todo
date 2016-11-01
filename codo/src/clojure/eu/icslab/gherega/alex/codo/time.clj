(ns eu.icslab.gherega.alex.codo.time
  (:require [joda-time :as joda :only [to-millis-from-epoch
                                        print
                                        date-time
                                        formatter]]
  ;;[clojure.data.xml :as xml]
  ;;[neko.activity :refer [defactivity set-content-view!]]
  ;;           [neko.debug :refer [*a]]
  ;;           [neko.notify :refer [toast]]
  ;;           [neko.resource :as res]
  ;;           [neko.find-view :refer [find-view]]
  ;;          [neko.threading :refer [on-ui]]
                                        ;[clj-time.coerce :as time-coerce]
                                        ;[clj-time.core :as time-core]
                                        ;[clj-time.format :as time-format]
  )
  ;; (:import android.widget.EditText
  ;;          android.widget.TextView)
)

(defn to-millis-from-epoch [date]
  (joda/to-millis-from-epoch date))

(defn date-time
  ([] (joda/date-time))
  ([date-timestamp] (joda/date-time date-timestamp)))

(defn formatter [date-string] (joda/formatter date-string))

(defn print [frmtter dt] (joda/print frmtter dt))
