(ns oxylab.model)

;
; Settings
;

;
; ( 0, 0) ( 1, 0)
;     ( 0, 1) ( 1, 1)
; ( 0, 2) ( 1, 2)
;
;
(def test-data
  [[0 0]
   [1 0]
   [0 1]
   [-1 1]
   [-1 0]
   [-1 -1]
   [0 -1]])

;
; Functions
;

(defn- init-cell [[x y]]
  "Returns default setting for the cell at given coordinates k"
  {:resources {:acid 1.0
               :detrit 0.002
               :soil 0.001}
   :populations {}
   :x x
   :y y})

;
; Interface
;

(def field
  (for [x (range -4 4)
        y (range -4 5)]
    [x y]))

(defn init-world []
  {:cells (->> test-data
               (map #(vector % (init-cell %)))
               (apply concat)
               (apply hash-map))
   :tick 0})

(defn can-evolve? [world [x y]]
  (let [t (+ x (mod y 2))
        near #{[(dec x) y]
               [(inc x) y]
               [(dec t) (dec y)]
               [t (dec y)]
               [(dec t) (inc y)]
               [t (inc y)]}]
    (->> (:cells world)
         (keys)
         (filter near)
         (seq))))

(defn evolve-cell [world [x y :as k]]
  "Add new cell with key k to the lab"
  (if (can-evolve? world k)
    (assoc-in world [:cells k] (init-cell k))
    world))

(defn update-world [world]
  "Main game state update funciton"
  (update-in world [:tick] inc))

(defn get-cell [world k]
  "Get cell at given coordinates k"
  (get-in world [:cells k]))
