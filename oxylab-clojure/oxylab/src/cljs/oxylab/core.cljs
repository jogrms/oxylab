(ns oxylab.core
  (:require [oxylab.model :as m]
            [oxylab.view :as v]
            [enfocus.core :as ef]
            [enfocus.events :as events]
            [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))

(declare cell-click)

(defn init-state []
  {:world (m/init-world)})

(def state (atom (init-state)))

(def fps 10)

(defn- update-state [state]
  (update-in state [:world] m/update-world))

(defn evolve-click [id]
  (swap! state update-in [:world] #(m/evolve-cell % (v/id->key id)))
  (ef/at [id] (v/cell-lab-transform))
  (cell-click id))

(defn cell-click [id]
  (swap! state assoc-in [:selected-cell] id)
  (ef/at ["#cell-info"] (ef/content (v/generate-cell-info-html @state id)))
  (ef/at ["#evolve-btn"] (events/listen :click #(evolve-click id))))

(defn generate-handlers []
  (doseq [cell m/field]
    (let [[x y] cell
          id (v/key->ids [x y])]
      (ef/at [id] (events/listen :click #(cell-click id))))))

(defn initial-render [state]
  (doseq [cell (get-in state [:world :cells])]
    (let [[x y] (first cell)]
      (ef/at [(v/key->ids [x y])] (v/cell-lab-transform)))))

(defn- render-lab-info [state]
  (ef/at ["#lab-info"] (ef/content (v/generate-lab-info-html state))))

(defn- render [state]
  (render-lab-info state)
  (v/render-app-info state))

(defn- next-frame []
  "Main loop. Update game state, render it and re-schedule next-frame call"
  (swap! state update-state)
  (render @state)
  ((.-setTimeout js/window) next-frame (/ 1000 fps)))

(defn- start []
  (ef/at js/document
    ["body"] (ef/content (v/generate-layout-html))
    ["#field"] (ef/content (v/generate-field-html)))
  (generate-handlers)
  (initial-render @state)
  (next-frame))

(defn ^:export main []
  (set! (.-onload js/window) start))

