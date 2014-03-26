(ns oxylab.script
  (:require [oxylab.species :as species]
            [oxylab.resources :as resources]
            [oxylab.utils :as u]))

;
; Influence
;

(defn no-influence [res size] res)

(defn- add-res-influence [infl [res factor]]
  (fn [resources size]
    (let [r (infl resources size)]
      (if-let [val (get r res)]
        (assoc r res (max 0 (+ val (* size factor))))
        r))))

(defn- get-delta-influence [inf]
  (reduce add-res-influence no-influence inf))

(defn- set-influence [spec]
  (update-in spec [:influence] get-delta-influence))

;
; Production
;

(defn- get-exp-production [max-size prod-rate]
  (fn [res size]
    (cond
     (>= size max-size) max-size
     (<= size 0) 0
     :else (let [res-factor 1.0]
             (+ size (* size prod-rate res-factor))))))

(defn- set-production [spec]
  (-> spec
      (assoc :production (get-exp-production (:max-size spec)
                                             (:production-rate spec)))
      (dissoc :production-rate)
      (dissoc :max-size)
      (dissoc :tolerance)))

;
; Interface
;

(defn init-species []
  (u/update-vals species/species
              (fn [spec] (-> spec
                             (set-production)
                             (set-influence)))))

(defn init-resources []
  resources/resources)
