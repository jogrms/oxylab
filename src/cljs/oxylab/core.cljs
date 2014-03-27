(ns oxylab.core
  (:require [oxylab.model :as m]
            [oxylab.view :as v]
            [enfocus.core :as ef]
            [enfocus.events :as events]
            [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))

(declare cell-click)

(defn init-state []
  {:tick 0
   :world (m/init-world)})

(def state (atom (init-state)))

(def fps 10)

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
  (v/render @state)
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

(defn- populate-click [species]
  (let [k (:selected-cell @state)]
    (swap! state update-in [:world] m/populate-cell k species))
  (v/render @state))

(defn- generate-populate-handlers []
  (doseq [spec (get-in @state [:world :species])]
    (let [n (name (first spec))
          id (str "#populate-" n)]
      (ef/at [id] (events/listen :click #(populate-click (first spec)))))))

;
; Controls
;

(def fps-out)
(def date)
(def fps-count 0)
(def *running* false)

(defn- update-state [state]
  (-> state
      (update-in [:world] m/update-world)
      (update-in [:tick] inc)))

(defn- next-frame []
  "Main loop. Update game state, render it and re-schedule next-frame call"
  (when *running*
    ((.-setTimeout js/window) next-frame (/ 1000 fps)))
  (swap! state update-state)
  (v/render @state)
  (if (= fps-count (dec fps))
    (let [d (.getTime (new js/Date))]
      (v/html! fps-out (subs (str (/ (* 1000 fps) (- d date))) 0 4))
      (set! date d)
      (set! fps-count 0))
    (set! fps-count (inc fps-count))))

(def start-btn)

(defn start-btn-click []
  (if *running*
    (do
      (set! *running* false)
      (set! (.-innerHTML start-btn) "start"))
    (do
      (set! *running* true)
      (set! (.-innerHTML start-btn) "stop")
      (set! date (.getTime (new js/Date)))
      (next-frame))))

(defn- main []
  (ef/at js/document
    ["body"] (ef/content (v/generate-layout-html @state))
    ["#field"] (ef/content (v/generate-field-html)))
  (generate-populate-handlers)
  (generate-handlers)
  (generate-populate-handlers)
  (initial-render @state)
  (set! fps-out (v/byid "fps"))
  (set! start-btn (v/byid "start-btn"))
  (set! (.-onclick start-btn) start-btn-click)
  (cell-click (v/key->id [0 0])))

(set! (.-onload js/window) main)

