(ns oxylab.core
  (:require [oxylab.model :as model]
            [enfocus.core :as ef]
            [enfocus.events :as events]
            [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))


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
  (let [[x2d y2d] (hex->2d coords)
        points [(+ x2d 4.9) (+ y2d 2.829)
                x2d (+ y2d 5.658)
                (- x2d 4.9) (+ y2d 2.829)
                (- x2d 4.9) (- y2d 2.829)
                x2d (- y2d 5.658)
                (+ x2d 4.9) (- y2d 2.829)]]
    [:polygon {:id (cell-id x y)
               :stroke "#777" :stroke-width 0.2 :fill "#444"
               :points (apply str (interpose \  points))}]))

(defn render-cells []
    (map render-cell field))

(defn cell-lab-transform []
  (ef/set-attr :fill "#964" :stroke "#fff"))

(defn cell-click [id]
  (ef/at [id] (cell-lab-transform)))

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

(defn initial-render [model]
  (doseq [cell (:cells model)]
    (let [[x y] (first cell)]
      (ef/at [(cell-ids x y)] (cell-lab-transform)))))

(defn start []
  (ef/at js/document
    ["body"] (ef/content (template))
    ["body"] (ef/set-attr :style "background-color: #444; align: center;"))
  (generate-handlers)
  (initial-render (model/init)))

(defn ^:export main []
  (set! (.-onload js/window) start))

