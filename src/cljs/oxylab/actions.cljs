(ns oxylab.actions
  (:require [oxylab.state :as s]
            [oxylab.model :as m]
            [cljs.reader :refer [read-string]]
            [clojure.set :refer [union]]))

(def fps 10)

(defn- update-state [state]
  (-> state
      (update-in [:world] m/update-world)
      (update-in [:tick] inc)))


(defn- next-frame! []

  "Main loop. Update game state, and re-schedule next-frame call"

  (when (:running @s/state)
    ((.-setTimeout js/window) next-frame! (/ 1000 fps)))
  (swap! s/state update-state)
  (if (= (:fps-count @s/state) (dec fps))
    (let [d (.getTime (new js/Date))
          fps-swapper (fn [state] (-> state
                                      (assoc :fps (/ (* 1000 fps) (- d (:date state))))
                                      (assoc :date d)
                                      (assoc :fps-count 0)))]
      (swap! s/state fps-swapper))
    (swap! s/state update-in [:fps-count] inc)))


(defn start! []
  (let [species (-> (js/document.getElementById "species-editor")
                    (.-value)
                    (read-string))
        resources (-> (js/document.getElementById "resources-editor")
                      (.-value)
                      (read-string))
        swapper (fn [state] (-> state
                                (assoc :errors #{})
                                (assoc :running true)
                                (assoc :world (m/init-world species resources))
                                (assoc :date (.getTime (new js/Date)))))]
    (if (and (map? species) (map? resources) (seq species) (seq resources))
      (do
        (swap! s/state swapper)
        (next-frame!))
      (let [err-set (union (when-not (and (map? species) (seq species)) #{:species})
                           (when-not (and (map? resources) (seq resources)) #{:resources}))]
        (swap! s/state assoc :errors err-set)))))


(defn stop! []
  (reset! s/state s/initial-state))

(defn pause! []
  (swap! s/state assoc :running false))

(defn resume! []
  (swap! s/state assoc :running true)
  (next-frame!))


(defn populate! [species]
  (swap! s/state update-in [:world] m/populate-cell [0 0] species))
