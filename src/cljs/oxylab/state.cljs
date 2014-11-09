(ns oxylab.state
  (:require [reagent.core :as reagent]))

(def initial-state {:tick-size 0.02})

(def state (reagent/atom initial-state))

(def working (reagent/atom false))
