(ns oxylab.model
  (:require [oxylab.species :as s]))

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
  "Returns default settings for the cell at given coordinates k"
  {:resources {:acid 1.0
               :detrit 0.002
               :soil 0.001}
   :populations {}})

(def field
  "List of playable cell coordinates"
  (for [x (range -4 4)
        y (range -4 5)]
    [x y]))

(defn init-world []
  "Get a brave new world"
  {:cells (->> test-data
               (map #(vector % (init-cell %)))
               (apply concat)
               (apply hash-map))
   :species (s/init-species)})

(defn can-populate? [world k species]
  "Check if cell k can be populated by given species in given world"
  (and (get-in world [:cells k])
       (get-in world [:species species])
       (not (get-in world [:cells k :populations species]))))

(defn populate-cell [world k species]
  "Populate cell k by given species and return updated world"
  (if (can-populate? world k species)
    (assoc-in world [:cells k :populations species]
              (get-in world [:species species]))
    world))

(defn can-evolve? [world [x y :as k]]
  "Check if cell k can be evolved in current world"
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
         (seq)
         (and (not (get-in world [:cells k]))))))

(defn evolve-cell [world [x y :as k]]
  "Add new cell with key k to the lab"
  (if (can-evolve? world k)
    (assoc-in world [:cells k] (init-cell k))
    world))

(defn update-world [world]
  "Main game state update funciton"
  world)

(defn get-cell [world k]
  "Get cell at given coordinates k"
  (get-in world [:cells k]))
