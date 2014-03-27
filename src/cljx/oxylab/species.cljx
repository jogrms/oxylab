(ns oxylab.species)

(def species
  {:scryopus {:size 1.0
              :max-size 10.0
              :production-rate 0.001
              :tolerance {:detrit {:model :range
                                   :ideal 5.0
                                   :radius 5.0}
                          :acid {:model :limit
                                 :ideal 5.0
                                 :radius 1.0}}
              :influence {:detrit -0.005
                          :soil 0.005}}

   :abracada {:size 2.0
              :max-size 1000.0
              :production-rate 0.05
              :tolerance {:soil {:model :limit
                                 :ideal 5.0
                                 :radius -3.0}}
              :influence {:soil -0.0005}}})
