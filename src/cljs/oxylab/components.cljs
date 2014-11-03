(ns oxylab.components
  (:require [reagent.core :as reagent]
            [clojure.pprint :refer [pprint]]

            [oxylab.state :as s]
            [oxylab.actions :as act])
  (:require-macros [oxylab.util.macros :refer [paste-resource]]))

(defn panel []
  [:div.row
   [:div.btn-group.col-xs-4
    [:a.btn.btn-success {:on-click act/start!
                         :class (when (:running @s/state) "disabled")}
     "Start"]
    [:a.btn.btn-default {:on-click act/pause!
                         :class (when-not (:running @s/state) "disabled")}
     "Pause"]
    [:a.btn.btn-default {:on-click act/resume!
                         :class (when (:running @s/state) "disabled")}
     "Resume"]
    [:a.btn.btn-warning {:on-click act/stop!}
     "Stop"]]
   (when (seq (get-in @s/state [:world :species]))
     [:div.btn-group.col-xs-8
      (for [[k spec] (get-in @s/state [:world :species])]
        [:a.btn.btn-primary {:on-click #(act/populate! k)} (name k)])])])

(defn bar [cap v max min class]
  [:div.row
   [:div.col-xs-2 cap]
   [:div.col-xs-4
    [:div.progress {:style {:height "15px"}}
     [:div.progress-bar
      {:class class
       :style
       {:width
        (str (* 100 (/ v max)) "%")}}]]]
   [:div.col-xs-2 (str min)]
   [:div.col-xs-2 (str v)]
   [:div.col-xs-2 (str max)]])

(defn bars []
  [:div
   [:h4 "Species bars"]
   (when (seq (get-in @s/state [:world :cells [0 0] :populations]))
     (for [[k p] (get-in @s/state [:world :cells [0 0] :populations])]
       [bar (str k) (:size p) (get-in @s/state [:world :species k :max-size]) (get-in @s/state [:world :species k :min-size]) "progress-bar-success"]))
   [:h4 "Resource bars"]
   (when (seq (get-in @s/state [:world :cells [0 0] :resources]))
     (for [[k v] (get-in @s/state [:world :cells [0 0] :resources])]
       [bar (str k) (:size v) (:max-size v) 0 "progress-bar-warning"]))])

(defn root []
  [:div {:style {:width "100%"
                 :padding "30px"}}
   [panel]
   [:div.row
    [:div.col-xs-8
     [bars]]
    [:div.col-xs-4
     [:h3 "app info:"]
     [:p (str "game time: " (/ (:tick @s/state) act/fps) " seconds")]
     [:pre.well.well-sm {:style {:color "#fff"}}
      (-> @s/state
          (dissoc :world)
          (pprint))]]
    [:div.row {:style {:height "500px"}}
     [:div.col-xs-6
      (when (contains? (:errors @s/state) :species)
        [:div.alert.alert-danger "Syntax error in species definition"])
      [:textarea#species-editor {:key "dont-delete-me1"
                                 :style {:width "100%"
                                         :height "100%"
                                         :font-family "Courier New"}
                                 :default-value (paste-resource "resources/species.clj")}]]
     [:div.col-xs-6
      (when (contains? (:errors @s/state) :resources)
        [:div.alert.alert-danger "Syntax error in resources definition"])
      [:textarea#resources-editor {:key "dont-delete-me2"
                                   :style {:width "100%"
                                           :height "100%"
                                           :font-family "Courier New"}
                                   :default-value (paste-resource "resources/resources.clj")}]]]]])

(defn start! []
  (reagent/render-component
    [root]
    js/document.body))
