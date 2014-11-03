(ns oxylab.components
  (:require [reagent.core :as reagent]
            [clojure.pprint :refer [pprint]]

            [oxylab.state :as s]
            [oxylab.actions :as act]

            [oxylab.species :as species]
            [oxylab.resources :as resources]))

(defn panel []
  [:div.btn-group
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
    "Stop"]
   [:div.btn-group
    [:a#populate-dd.btn.btn-default.dropdown-toggle
     {:data-toggle "dropdown"
      :class (when-not (:running @s/state) "disabled")}
     "populate cell "
     [:span.caret]]
    [:ul.dropdown-menu {:aria-labelledby "populate-dd"}
     (for [[k spec] (get-in @s/state [:world :species])]
       [:li [:a {:on-click #(act/populate! k)} (name k)]])]]])

(defn bar [cap v max]
  [:div.row
   [:div.col-xs-4 cap]
   [:div.col-xs-8
    [:div.progress {:style {:height "15px"}}
     [:div.progress-bar.progress-bar-success
      {:style
       {:width
        (str (* 100 (/ v max)) "%")}}]]]])

(defn bars []
  [:div
   [:h5 "Species bars"]
   (when (seq (get-in @s/state [:world :cells [0 0] :populations]))
     (for [[k p] (get-in @s/state [:world :cells [0 0] :populations])]
       [bar (str k) (:size p) (get-in @s/state [:world :species k :max-size])]))
   [:h5 "Resource bars"]
   (when (seq (get-in @s/state [:world :cells [0 0] :resources]))
     (for [[k v] (get-in @s/state [:world :cells [0 0] :resources])]
       [bar (str k) (:size v) (:max-size v)]))])

(defn root []
  [:div.container
   [panel]
   [:div.row
    [:div.col-xs-4
     [bars]]
    [:div.col-xs-4
     [:h3 "lab info:"]
     [:pre.well.well-sm {:style {:color "#fff"}}
      (pprint (get-in @s/state [:world :cells [0 0]]))]]
    [:div.col-xs-4
     [:h3 "app info:"]
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
                                :default-value (pprint species/species)}]]
    [:div.col-xs-6
     (when (contains? (:errors @s/state) :resources)
       [:div.alert.alert-danger "Syntax error in resources definition"])
     [:textarea#resources-editor {:key "dont-delete-me2"
                                  :style {:width "100%"
                                          :height "100%"
                                          :font-family "Courier New"}
                                  :default-value (pprint resources/resources)}]]]]])

(defn start! []
  (reagent/render-component
    [root]
    js/document.body))
