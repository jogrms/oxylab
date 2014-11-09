(ns oxylab.model
  (:require [oxylab.script :as script]
            [oxylab.utils :as u]))

(defn- init-cell [res]
  "Returns default settings for the cell at given coordinates k"
  {:resources (script/init-resources res)
   :populations {}})

(defn init-world [spec res]
  "Get a brave new world"
  {:cell (init-cell res)
   :species (script/init-species spec)})

(defn can-populate? [world species]
  "Check if cell k can be populated by given species in given world"
  (and (get-in world [:species species])
       (not (get-in world [:cell :populations species]))))

(defn populate-cell [world species]
  "Populate cell k by given species and return updated world"
  (if (can-populate? world species)
    (assoc-in world [:cell :populations species]
              (get-in world [:species species]))
    world))

(defn- readd-pop [cell pops [k p]]
  (let [new-size ((:production p) (:resources cell) (:size p))]
    (if (< new-size (:min-size p))
      pops
      (->> new-size
           (assoc p :size)
           (assoc pops k)))))

(defn- update-population-sizes [cell]
  (assoc cell :populations
    (reduce #(readd-pop cell %1 %2) {} (:populations cell))))


(defn- check-res [{:keys [size max-size] :as res}]
  (assoc res :size (min size max-size)))

(defn- update-resources [cell]
  (assoc cell :resources
    (->
     (reduce (fn [res [_ p]] ((:influence p) res (:size p)))
             (:resources cell)
             (:populations cell))
     (u/update-vals check-res))))

(defn- update-cell [cell]
  (-> cell
      (update-population-sizes)
      (update-resources)))

(defn update-world [world]
  "Main game state update funciton"
  (update-in world [:cell] update-cell))
