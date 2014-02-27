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

(defn init-cell [x y]
  {:acid 1.0
   :detrit 0.002
   :soil 0.001
   :x x
   :y y})

;
; Interface
;

(def field
  (for [x (range -4 4)
        y (range -4 5)]
    [x y]))

(defn ^:export evolve-cell [world k]
  (assoc-in world [:cells k] (apply init-cell k)))


(defn ^:export init []
  {:cells (apply hash-map
                 (apply concat
                        (map #(vector %1 (apply init-cell %1)) test-data)))})

(defn ^:export get-cell
  [world x y] (get (:cells world) [x y]))
