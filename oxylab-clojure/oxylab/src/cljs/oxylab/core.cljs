(ns oxylab.core
  (:require [oxylab.model :as m]
            [oxylab.view :as v]
            [enfocus.core :as ef]
            [enfocus.events :as events]
            [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))

(def world (atom (m/init-world)))

(declare cell-click)

(defn evolve-click [id]
  (swap! world m/evolve-cell (v/id->cell id))
  (ef/at [id] (v/cell-lab-transform))
  (cell-click id))

(defn cell-click [id]
  (ef/at ["#info"] (ef/content (v/generate-info-html
                                 (get-in @world [:cells (v/id->cell id)]))))
  (ef/at ["#evolve-btn"] (events/listen :click #(evolve-click id))))

(defn generate-handlers []
  (doseq [cell m/field]
    (let [[x y] cell
          id (v/cell-ids x y)]
      (ef/at [id] (events/listen :click #(cell-click id))))))

(defn initial-render [world]
  (doseq [cell (:cells world)]
    (let [[x y] (first cell)]
      (ef/at [(v/cell-ids x y)] (v/cell-lab-transform)))))

(defn start []
  (ef/at js/document
    ["body"] (ef/content (v/generate-layout-html))
    ["#field"] (ef/content (v/generate-field-html)))
  (generate-handlers)
  (initial-render @world))

(defn ^:export main []
  (set! (.-onload js/window) start))

