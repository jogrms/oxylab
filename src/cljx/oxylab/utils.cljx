(ns oxylab.utils)

(defn update-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))
