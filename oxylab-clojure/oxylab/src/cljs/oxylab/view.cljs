(ns oxylab.view
  (:require [enfocus.core :as ef]
            [oxylab.model :as m]
            [cljs.reader :as reader]))

(def bg-color "#272b30")
(def lab-fill-color "#530")
(def selected-stroke-color "#fff")
(def lab-stroke-color "#aaa")
(def field-stroke-color "#444")

(defn key->id [[x y]]
  (str "cell" x "x" y))

(defn key->ids [k]
  (str "#" (key->id k)))

(defn id->key [id]
  (apply vector (map reader/read-string
    (next (clojure.string/split id #"[celx]+")))))

(defn key->coord [[x y]]
  [(+ 50 (+ (* (mod y 2) 5) (* x 10)))
   (+ 50 (* y 8.66))])

(defn ->paragraphs [s]
  (map #(vector :p (str %)) s))

(defn render-cell [[x y :as coords]]
  (let [[x2d y2d] (key->coord coords)
        points [(+ x2d 4.9) (+ y2d 2.829)
                x2d (+ y2d 5.658)
                (- x2d 4.9) (+ y2d 2.829)
                (- x2d 4.9) (- y2d 2.829)
                x2d (- y2d 5.658)
                (+ x2d 4.9) (- y2d 2.829)]]
    [:polygon {:id (key->id [x y])
               :stroke field-stroke-color :stroke-width 0.2 :fill bg-color
               :points (apply str (interpose \  points))}]))

(defn render-cells []
    (map render-cell m/field))

(defn evolve-cell [state id]
  (ef/at [(str \# id)] (ef/set-attr :fill lab-fill-color
                                    :stroke lab-stroke-color)))

(defn select-cell [state id]
  (ef/at [(str \# id)] (ef/set-attr :stroke selected-stroke-color
                                    :stroke-width 0.4)))

(defn unselect-cell [state id]
  (let [k (id->key id)
        cell (get-in state [:world :cells k])
        color (if cell lab-stroke-color field-stroke-color)]
    (ef/at [(str \# id)] (ef/set-attr :stroke color
                                      :stroke-width 0.2))))

(defn generate-cell-info-html [state id]
  (let [k (id->key id)
        cell (get-in state [:world :cells k])]
    (ef/html
         (if (nil? cell)
           [:div
            [:h2 "cell info:"]
            [:p "This cell does not belong to your Lab"]
            (if (m/can-evolve? (:world state) k)
              [:p [:a.btn#evolve-btn "Evolve"]]
              [:p])]
           [:div
            [:h2 "cell info:"]
            [:p (str "coordinates: " (:x cell) ", " (:y cell))]
            [:h3 "resources:"]
            (->> (:resources cell)
                 (->paragraphs))
            [:h3 "populations:"]
            (->> (:populations cell)
                 (->paragraphs))]))))

(defn generate-layout-html []
  (ef/html
    [:div.container
     [:div.row
      [:div.col-xs-6#field]
      [:div.col-xs-3#cell-info {:style "padding-top: 40px;"}]
      [:div.col-xs-2#lab-info {:style "padding-top: 40px;"}]]
     [:div.row
      [:div#app-info]]]))

(defn generate-lab-info-html [state]
  (ef/html
    [:div
     [:h2 "lab info:"]
     (->> (:world state)
          (seq)
          (filter (comp not #{:cells} first))
          (->paragraphs))]))

(defn- generate-app-info-html [state]
  (ef/html
    [:div
     [:h2 "app info:"]
     (->> (seq state)
          (filter (comp not #{:world} first))
          (->paragraphs))]))

(defn generate-field-html [] (.-outerHTML
  (ef/html 
    [:svg {:width "525" :height "500" :viewbox "-5 0 105 100"}
     [:rect {:width 100 :height 100 :fill bg-color}]
     (render-cells)])))

(defn- render-field [old-state state]
  nil)

(defn- render-lab-info [old-state state]
  (ef/at ["#lab-info"] (ef/content (generate-lab-info-html state))))

(defn- render-app-info [old-state state]
  (ef/at ["#app-info"] (ef/content (generate-app-info-html state))))

(defn render [old-state state]
  (render-field old-state state)
  (render-lab-info old-state state)
  (render-app-info old-state state))







