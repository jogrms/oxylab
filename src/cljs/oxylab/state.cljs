(ns oxylab.state
  (:require [reagent.core :as reagent]))

(def initial-state {:tick 0
                    :running false
                    :fps-count 0
                    :fps 0})

(def state (reagent/atom initial-state))
