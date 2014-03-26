(ns oxylab.species
  (:require [oxylab.species.utils :as u]))

(defn init-species []
  "Species definition.

  :size - initial size of population

  :production - production function ([resources size] -> new-size)
    (u/exp-production max-size base) - exponential population growth
      limited by max-size. (new-size = size * base)

  :influence - influence function ([resources size] -> new-resources)
    u/no-influence - the species doesn't change any resources"

  {:scryopus {:size 1.0
              :production (u/exp-production 10.0 1.001)
              :influence (u/add-influence :detrit -0.005)}

   :abracadabra {:size 2.0
                 :production (u/exp-production 10.0 1.05)
                 :influence u/no-influence}})
