(ns oxylab.actions
  (:require [oxylab.state :as s]
            [oxylab.model :as m]
            [cljs.reader :refer [read-string]]
            [clojure.set :refer [union]]))

(defn plot [world tick max-tick populate-script]
  (loop [acc [{:world world
               :tick tick}]]
    (if (< (:tick (last acc)) max-tick)
      (let [w (:world (last acc))
            populated-world (if-let [spec (get populate-script (:tick (last acc)))]
                              (m/populate-cell w spec)
                              w)
            updated-world (m/update-world populated-world)]
        (recur (conj acc {:world updated-world
                          :tick (inc (:tick (last acc)))})))
      acc)))

(defn apply! []
  (let [species (-> (js/document.getElementById "species-editor")
                    (.-value)
                    (read-string))
        resources (-> (js/document.getElementById "resources-editor")
                      (.-value)
                      (read-string))
        secs (-> (js/document.getElementById "tick-count-editor")
                       (.-value)
                       (read-string))
        colors (-> (js/document.getElementById "colors-editor")
                       (.-value)
                       (read-string))
        tick-count (* 10 secs)
        populate-script (-> (js/document.getElementById "populate-script-editor")
                            (.-value)
                            (read-string))
        swapper (fn [state]
                  (let [world (m/init-world species resources)]
                    (-> state
                        (assoc :tick-count tick-count)
                        (assoc :errors #{})
                        (assoc :world world)
                        (assoc :colors colors)
                        (assoc :plot (plot world 0 tick-count populate-script)))))]
    (if (and (map? species) (map? resources) (seq species) (seq resources))
      (do
        (reset! s/working true)
        (swap! s/state swapper)
        (reset! s/working false))
      (let [err-set (union (when-not (and (map? species) (seq species)) #{:species})
                           (when-not (and (map? resources) (seq resources)) #{:resources}))]
        (swap! s/state assoc :errors err-set)))))

