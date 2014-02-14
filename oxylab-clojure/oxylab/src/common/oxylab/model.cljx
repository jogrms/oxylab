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

(defn init-cell []
  {:acid 1.0
   :detrit 0.0
   :soil 0.0})

;
; Interface
;

(defn ^:export init []
  {:cells (apply hash-map
                 (apply concat
                        (map #(vector %1 (init-cell)) test-data)))})

(defn ^:export get-cell
  [world x y] (get (:cells world) [x y]))
