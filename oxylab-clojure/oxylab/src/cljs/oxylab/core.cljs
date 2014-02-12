(ns oxylab.core
  (:require [oxylab.model :as model]
            [enfocus.core :as ef]
            [enfocus.events :as events]
            [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))

;
; ( 0, 0) ( 1, 0)
;     ( 0, 1) ( 1, 1)
; ( 0, 2) ( 1, 2)
;
;
(def test-data
  {:cells { [0 0] {}
            [1 0] {}
            [0 1] {}
            [-1 1] {}}})

(def field
  (for [x (range -5 5)
        y (range -5 5)]
    [x y]))

(defn cell-id [x y]
  (str "cell" x "x" y))

(defn cell-ids [x y]
  (str "#" (cell-id x y)))

(defn hex->2d [[x y]]
  [(+ 50 (+ (* (mod y 2) 5) (* x 10)))
   (+ 50 (* y 8.66))])

(defn render-cell [[x y :as coords]]
  (let [[x2d y2d] (hex->2d coords)]
      [:circle {:cx x2d :cy y2d :r 5 :id (cell-id x y)
                :stroke "#777" :stroke-width 0.2 :fill "#444"}]))

(defn render-cells []
    (map render-cell field))

(defn cell-click [id]
  (ef/at [id] (ef/set-attr :fill "#774")))

(defn generate-handlers []
  (doseq [cell field]
    (let [[x y] cell
          id (cell-ids x y)]
      (ef/at [id] (events/listen :click #(cell-click id))))))

(defn template [] (.-innerHTML
  (ef/html 
    [:div
     [:svg {:width "525" :height "500" :viewbox "-5 0 105 100"}
      [:rect {:width 100 :height 100 :fill "#444"}]
      (render-cells)]])))

(defn start []
  (ef/at js/document
    ["body"] (ef/content (template))
    ["body"] (ef/set-attr :style "background-color: #444; align: center;"))
  (generate-handlers))

(defn ^:export main []
  (set! (.-onload js/window) start))

