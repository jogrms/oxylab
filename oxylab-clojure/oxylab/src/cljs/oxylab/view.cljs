(ns oxylab.view
  (:require [enfocus.core :as ef]
            [oxylab.model :as m]
            [cljs.reader :as reader]))

(def bg-color "#272b30")

(defn cell-id [x y]
  (str "cell" x "x" y))

(defn cell-ids [x y]
  (str "#" (cell-id x y)))

(defn id->cell [id]
  (apply vector (map reader/read-string
    (next (clojure.string/split id #"[celx]+")))))

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
               :stroke "#444" :stroke-width 0.2 :fill bg-color
               :points (apply str (interpose \  points))}]))

(defn render-cells []
    (map render-cell m/field))

(defn cell-lab-transform []
  (ef/set-attr :fill "#964" :stroke "#fff"))

(defn generate-info-html [props]
  (ef/html
       (if (nil? props)
         [:div
          [:h3 "cell info:"]
          [:p "This cell does not belong to your Lab"]
          [:p [:a.btn#evolve-btn "Evolve"]]]
         [:div
          [:h3 "cell info:"]
          (map #(vector :p (str %1)) (seq props))])))

(defn generate-layout-html []
  (ef/html
    [:div.container
     [:div.row
      [:div.col-xs-6#field]
      [:div.col-xs-3#info {:style "padding-top: 40px;"}]
      [:div.col-xs-2#ginfo {:style "padding-top: 40px;"}
       [:h3 "lab info:"]]]]))

(defn generate-field-html [] (.-outerHTML
  (ef/html 
      [:svg {:width "525" :height "500" :viewbox "-5 0 105 100"}
       [:rect {:width 100 :height 100 :fill bg-color}]
       (render-cells)])))







