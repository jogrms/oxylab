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

(def fps 30)

(defn- update-state [state]
  (-> state
      (update-in [:world] m/update-world)
      (update-in [:tick] inc)))

(defn evolve-click [id]
  (let [k (v/id->key id)]
    (swap! state update-in [:world] m/evolve-cell k)
    (when (get-in @state [:world :cells k])
      (v/evolve-cell @state id))
    (cell-click id)))

(defn cell-click [id]
  (v/unselect-cell @state (v/key->id (:selected-cell @state)))
  (v/select-cell @state id)
  (swap! state assoc-in [:selected-cell] (v/id->key id))
  (ef/at ["#cell-info"] (ef/content (v/generate-cell-info-html @state id)))
  (set! (.-onclick (v/byid "evolve-btn")) #(evolve-click id)))

(defn generate-handlers []
  (doseq [cell m/field]
    (let [[x y] cell
          id (v/key->ids [x y])]
      (ef/at [id] (events/listen :mousedown #(cell-click id))))))

(defn initial-render [state]
  (doseq [cell (get-in state [:world :cells])]
    (let [[x y] (first cell)]
      (v/evolve-cell state (v/key->id [x y])))))

(def fps-out)
(def date)
(def fps-count 0)

(defn- next-frame []
  "Main loop. Update game state, render it and re-schedule next-frame call"
  ((.-setTimeout js/window) next-frame (/ 1000 fps))
  (let [old-state @state]
    (swap! state update-state)
    (v/render old-state @state))
  (if (= fps-count (dec fps))
    (let [d (.getTime (new js/Date))]
      (v/html! fps-out (subs (str (/ (* 1000 fps) (- d date))) 0 4))
      (set! date d)
      (set! fps-count 0))
    (set! fps-count (inc fps-count))))

(defn- start []
  (ef/at js/document
    ["body"] (ef/content (v/generate-layout-html))
    ["#field"] (ef/content (v/generate-field-html)))
  (generate-handlers)
  (initial-render @state)
  (set! fps-out (v/byid "fps"))
  (set! date (.getTime (new js/Date)))
  (next-frame))

(defn ^:export main []
  (set! (.-onload js/window) start))

