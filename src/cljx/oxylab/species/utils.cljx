(ns oxylab.species.utils)

(defn no-influence [res size]
  res)

(defn add-influence [res-name amount]
  (fn [res size]
    (update-in res [res-name] #(max 0 (+ % (* amount size))))))

(defn prod [max-size size]
  "Cut range of size into [0 .. max-size]"
  (max 0 (min size max-size)))

(defn exp-production [max-size base]
  "Get exponential production function.
  new-size = size * base"
  (fn [res size] (prod max-size (* size base))))