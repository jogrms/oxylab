(ns oxylab.species)

(def species
{:cyanobacteria {:size 0.3e20
               :max-size 1e20
               :min-size 100
               :production-rate 0.02
               :influence {:detrit 1e-20
                           :oxygen 1e-20}}
:lichens {:size 0.3e6
         :max-size 1e6
         :min-size 10
         :production-rate 2e-3
         :influence {:detrit 0.4e-6
                     :oxygen 0.2e-6}}
:soilbacteria {:size 0.3e30
              :max-size 1e30
              :min-size 100
              :production-rate 0.05
              :tolerance {:detrit {:model :limit
                                   :ideal 1.2e-30
                                   :radius -1.2e-30}}
              :influence {:detrit -0.9e-30
                          :soil 1e-30}}
:fungus {:size 0.3e9
        :max-size 1e10
        :min-size 100
        :production-rate 1e-2
        :tolerance {:detrit {:model :limit
                             :ideal 1.4e-10
                             :radius -1.4e-10}}
        :influence {:detrit -1e-10
                    :soil 1.3e-10}}
:moss {:size 0.3e6
      :max-size 1e6
      :min-size 10
      :production-rate 1e-5
      :tolerance {:soil {:model :limit
                         :ideal 1
                         :radius -0.5}}
      :influence {:detrit 3e-6
                  :oxygen 3e-6
                  :soil -1e-6}}
:herbs {:size 300
       :max-size 1e4
       :min-size 5
       :production-rate 0.01
       :tolerance {:soil {:model :limit
                          :ideal 1.3
                          :radius -0.6}}
       :influence {:detrit 5e-4
                   :oxygen 4e-4
                   :soil -1.2e-4}}
:worms {:size 500
       :max-size 1e3
       :min-size 3
       :production-rate 1e-3
       :tolerance {:detrit {:model :limit
                            :ideal 8
                            :radius -5}}
       :influence {:detrit -9e-3
                   :soil 6e-3}}
:insects {:size 1e4
         :max-size 1e4
         :min-size 2
         :production-rate 0.39e-1
         :tolerance {:detrit {:model :limit
                              :ideal 10
                              :radius -6}}
         :influence {:detrit -13e-4
                     :soil 10e-4}}
:bushes {:size 2
        :max-size 100
        :min-size 2
        :production-rate 2e-3
        :tolerance {:soil {:model :limit
                           :ideal 6
                           :radius -1}}
        :influence {:detrit 6e-2
                    :oxygen 0.2
                    :soil -0.06}}
:foliar_trees {:size 1
              :max-size 20
              :min-size 1
              :production-rate 1e-3
              :tolerance {:soil {:model :limit
                                 :ideal 10
                                 :radius -1}}
              :influence {:detrit 0.4
                          :oxygen 2
                          :soil -0.5}}})
