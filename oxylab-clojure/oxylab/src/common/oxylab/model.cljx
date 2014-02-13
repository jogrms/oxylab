(ns oxylab.model)

;
; ( 0, 0) ( 1, 0)
;     ( 0, 1) ( 1, 1)
; ( 0, 2) ( 1, 2)
;
;
(def test-data
  {:cells { [0 0] {}
            [1 0] {}
            [0 1] {}
            [-1 1] {}}})

(defn ^:export init []
  test-data)
