(ns oxylab.script
  (:require [oxylab.utils :as u]))

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

(defn- get-quadratic-term-fn [{:keys [model ideal radius]}]
  (fn [v]
    (if (and (= model :limit)
             (>= (* (- ideal v) radius) 0))
      0
      (let [a (/ (- ideal v) radius)]
        (* a a)))))

(defn- get-quadratic-res-factor-fn [tols]
  (let [term-fns (u/update-vals tols get-quadratic-term-fn)]
    (fn [res]
      (- 1
         (apply + (for [[k f] term-fns] (f (get res k))))))))

(defn- prod [max-size size] (max 0 (min max-size size)))

(defn- get-exp-production [{:keys [production-rate max-size tolerance]}]
  (let [res-factor-fn (get-quadratic-res-factor-fn tolerance)]
    (fn [res size]
      (prod max-size
            (let [res-factor (res-factor-fn res)]
              (+ size (* size production-rate res-factor)))))))

(defn constant-production [res size] size)

(defn- set-production [spec]
  (-> spec
      (assoc :production (get-exp-production spec))
      ;(assoc :production constant-production)
      (dissoc :production-rate)
      ;(dissoc :max-size)
      (dissoc :tolerance)))

;
; Interface
;

(defn init-species [specs]
  (u/update-vals specs
              (fn [spec] (-> spec
                             (set-production)
                             (set-influence)))))

(defn init-resources [res]
  res)
