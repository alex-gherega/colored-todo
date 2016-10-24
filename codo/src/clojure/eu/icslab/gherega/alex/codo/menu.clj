(ns eu.icslab.gherega.alex.codo.menu
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.action-bar :refer [setup-action-bar tab-listener]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.resource :as res]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [clojure.data.json :as json]
            [eu.icslab.gherega.alex.codo.utils :as utils]
            )
  (:import android.widget.EditText
           android.widget.TextView
           android.widget.Button
           android.text.Html))
