(ns oxylab.core
  (:require [oxylab.model :as m]
            [oxylab.view :as v]
            [oxylab.components :as cmp]
            [oxylab.state :as s]
            [cljs.reader :refer [read-string]]))

(enable-console-print!)

(defn- main []
  (cmp/start!))

(set! (.-onload js/window) main)

