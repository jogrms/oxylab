(ns oxylab.state
  (:require [reagent.core :as reagent]))

(def state (reagent/atom {:tick 0
                          :running false
                          :fps-count 0
                          :fps 0}))
