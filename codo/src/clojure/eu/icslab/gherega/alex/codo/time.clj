(ns eu.icslab.gherega.alex.codo.time
  (:require [joda-time :as joda :only [to-millis-from-epoch
                                        print
                                        date-time
                                        formatter]]))

(defn to-millis-from-epoch [date]
  (joda/to-millis-from-epoch date))

(defn date-time
  ([] (joda/date-time))
  ([date-timestamp] (joda/date-time date-timestamp)))

(defn formatter [date-string] (joda/formatter date-string))

(defn print [frmtter dt] (joda/print frmtter dt))
