(ns oxylab.view
  (:require [enfocus.core :as ef]
            [oxylab.model :as m]
            [cljs.reader :as reader]
            [oxylab.utils :as u]
            [oxylab.species :as species]
            [goog.string :as gstring])
  (:use [clojure.pprint :only [pprint]]))

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
  [(+ (* (mod y 2) 5) (* x 10))
   (+ 10 (* y 8.66))])

(defn clear-borders [m]
  {:style (str "margin: 0px 0px 0px " m "px; border: 0px; padding: 0px;")})

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

(defn- info-container [id]
  [:pre.well.well-sm {:id id :style "color: #fff;"}])

(defn generate-layout-html [state]
  (ef/html
    [:div.container
     [:div.btn-group
      [:a.btn.btn-default#start-btn "start"]
      [:a.btn.btn-default#evolve-btn "evolve cell"]
      [:div.btn-group
       [:a.btn.btn-default.dropdown-toggle#populate-dd
        {:data-toggle "dropdown"}
        "populate cell "
        [:span.caret]]
       [:ul.dropdown-menu {:aria-labelledby "populate-dd"}
        (for [spec (get-in state [:world :species])]
          (let [n (name (first spec))
                id (str "populate-" n)]
            [:li [:a {:id id} n]]))]]]
     [:div.row
      [:div.col-xs-3#field]
      [:div.col-xs-3
       [:h2 "cell info:"]
       [:div#cell-dia]
       (info-container "cell-info")]
      [:div.col-xs-3
       [:h2 "lab info:"]
       (info-container "lab-info")]
      [:div.col-xs-3
       [:h2 "app info:"]
       [:p [:span#fps] " FPS"]
       (info-container "app-info")]]]))

(defn- generate-cell-info-html [cell]
  (if cell
    (-> cell
        (pprint)
        (str))
    ""))

(defn- generate-cell-dia-html [pops]
  (let [buf (gstring/StringBuffer.)]
    (doseq [[k v] pops]
      (.append buf
               "<div class='row'>
                 <div class='col-xs-4'>
                   <p>")
      (.append buf (name k))
      (.append buf
               "   </p>
                 </div>
                 <div class='col-xs-8'>
                   <div class='progress'>
                     <div class='progress-bar' style='width: ")
      (.append buf (str (* 100 (/ v (get-in species/species [k :max-size])))))
      (.append buf "%;'></div>
                   </div>
                 </div>
               </div>"))
    (str buf)))

(defn- generate-lab-info-html [state]
  (-> (:world state)
      (dissoc :cells)
      (pprint)
      (str)))

(defn- generate-app-info-html [state]
  (-> state
      (dissoc :world)
      (pprint)
      (str)))

(defn byid [id]
  (.getElementById js/document id))

(defn html! [node s]
  (set! (.-innerHTML node) s))

(defn generate-field-html [] (.-outerHTML
  (ef/html
    [:svg {:width "275" :height "250" :viewbox "-5 0 55 50"}
     [:rect {:width 50 :height 50 :fill bg-color}]
     (render-cells)])))

(defn- render-field [state]
  nil)

(defn- render-lab-info [state]
  (ef/at ["#lab-info"] (ef/content (generate-lab-info-html state))))

(defn- render-app-info [state]
  (ef/at ["#app-info"] (ef/content (generate-app-info-html state))))

(defn- render-cell-info [state]
  (let [k (:selected-cell state)
        cell (get-in state [:world :cells k])
        pops (u/update-vals (:populations cell) :size)
        cell-view (if cell (assoc cell :populations pops) nil)]
    (ef/at ["#cell-info"] (ef/content (generate-cell-info-html cell-view)))
    (ef/at ["#cell-dia"] (ef/content (generate-cell-dia-html pops)))))

(defn render [ state]
  (render-field state)
  (render-cell-info state)
  (render-lab-info state)
  (render-app-info state))
