(ns oxylab.components
  (:require [reagent.core :as reagent]
            [clojure.pprint :refer [pprint]]

            [oxylab.state :as s]
            [oxylab.actions :as act])
  (:require-macros [oxylab.util.macros :refer [paste-resource]]))

(defn panel []
  [:div.row
   [:div.btn-group.col-xs-4
    [:a.btn.btn-success {:on-click act/apply!}
     "Apply"]
    [:a.btn.btn-default
     {:on-click #(swap! s/state update-in [:tick-size] * 2)}
     "zoom in"]
    [:a.btn.btn-default
     {:on-click #(swap! s/state update-in [:tick-size] * 0.5)}
     "zoom out"]]])

(defn reducer [spec tick-size]
  (fn [s state]
    (let [tick (:tick state)
          world (:world state)
          p (get-in world [:cell :populations spec])
          v (if p
              (- 1 (/ (:size p) (:max-size p)))
              1)]
      (if p
        (str s " " (* tick tick-size) "," v)
        s))))

(defn points [plot spec tick-size]
  (reduce (reducer spec tick-size) "" plot))

(defn res-reducer [res-id tick-size]
  (fn [s state]
    (let [tick (:tick state)
          world (:world state)
          res (get-in world [:cell :resources res-id])
          v (- 2.05 (/ (:size res) (:max-size res)))]
      (str s " " (* tick tick-size) "," v))))

(defn res-points [plot res-id tick-size]
  (reduce (res-reducer res-id tick-size) "" plot))

(defn plot []
  [:div {:style {:width "100%"
                 :height "430px"
                 :overflow :auto
                 :margin "20px"}}
   (let [tick-size (:tick-size @s/state)
         tick-count (:tick-count @s/state)
         plt (:plot @s/state)
         colors (:colors @s/state)]
     [:svg {:key "svg"
            :style {:width (str (int (* (:tick-size @s/state) (:tick-count @s/state) 200)) "px")
                    :height "405px"
                    :stroke-width 0.01}
            :viewBox (str "0 0 " (* (:tick-size @s/state) (:tick-count @s/state)) " 2.05")}
      (for [i (range (int (/ (:tick-count @s/state) 50)))]
        (let [x (* 50 tick-size i)]
          [:g
           [:polyline {:style {:fill :none
                               :stroke "#555"
                               }
                       :points (str x "," 0 " " x "," 2.05)}]
           [:text {:x x
                   :y 2.05
                   :style {:fill "#fff"
                           :font-size 0.08}}
            (str (* 5 i) "s")]]))
      [:polyline {:style {:fill :none
                          :stroke "#555"}
                  :points (str 0 "," 1 " " (* tick-size tick-count) "," 1)}]
      (for [[k spec] (get-in @s/state [:world :species])]
        [:polyline {:style {:fill :none
                            :stroke (get colors k)}
                    :points (points plt k tick-size)}])


      (for [[res-id _] (get-in @s/state [:world :cell :resources])]
        [:polyline {:style {:fill :none
                            :stroke (get colors res-id)}
                    :points (res-points plt res-id tick-size)}])
      ])])

(defn colors []
  [:div
   (for [[k v] (:colors @s/state)]
     [:div.row
      [:div.col-xs-8 (str k)]
      [:div.col-xs-4
       {:style {:background-color v
                :height "15px"}}]])])

(defn root []
  [:div {:style {:width "100%"
                 :padding "30px"}}
   [panel]
   [:div.row
    [:div.col-xs-10
     [plot]]
    [:div.col-xs-2
     [colors]]
    [:div.row {:style {:height "500px"}}
     [:div.col-xs-6
      (when (contains? (:errors @s/state) :species)
        [:div.alert.alert-danger "Syntax error in species definition"])
      [:textarea#species-editor {:key "dont-delete-me1"
                                 :style {:width "100%"
                                         :height "100%"
                                         :font-family "Courier New"}
                                 :default-value (paste-resource "resources/species.clj")}]]
     [:div.col-xs-3
      [:div.row {:style {:height "150px"
                         :margin-bottom "10px"}}
       [:textarea#resources-editor {:key "dont-delete-me2"
                                    :style {:width "100%"
                                            :height "100%"
                                            :font-family "Courier New"}
                                    :default-value (paste-resource "resources/resources.clj")}]]
      [:div.row {:style {:height "340px"}}
       [:textarea#colors-editor {:key "dont-delete-me4"
                                    :style {:width "100%"
                                            :height "100%"
                                            :font-family "Courier New"}
                                    :default-value (paste-resource "resources/colors.clj")}]]]
     [:div.col-xs-3
      [:p "Time frame: " [:input#tick-count-editor {:key "dont-delete-me-4"
                                                    :type :number
                                                    :default-value 60
                                                    :style {:width "50px"}}]
       " seconds"]
      (when (contains? (:errors @s/state) :populate)
        [:div.alert.alert-danger "Syntax error in populate script"])
      [:textarea#populate-script-editor {:key "dont-delete-me3"
                                   :style {:width "100%"
                                           :height "100%"
                                           :font-family "Courier New"}
                                   :default-value (paste-resource "resources/populate.clj")}]]]]])

(defn start! []
  (reagent/render-component
    [root]
    js/document.body))
