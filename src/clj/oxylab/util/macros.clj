(ns oxylab.util.macros)

(defmacro paste-resource [path]
  (slurp path))
