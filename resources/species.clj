{:cyanobacteria {:size 1e19
                 :max-size 1e20
                 :min-size 1e18
                 :production-rate 0.015
                 :influence {:detrit 0.3e-20}
                 :tolerance {:detrit {:model :limit
                                      :ideal 35
                                      :radius 15}
                             :soil {:model :limit
                                    :ideal 19.5
                                    :radius 0.1}}}
:soilbacteria {:size 3e19
                :max-size 1e20
                :min-size 1e18
                :production-rate 0.01
                :tolerance {:detrit {:model :limit
                                     :ideal 35
                                     :radius -10}                       
                            :soil {:model :limit
                                   :ideal 22
                                   :radius 1}}
                :influence {:detrit -0.35e-20
                            :soil 0.045e-20}}
 :lichens {:size 1e4
                 :max-size 1e5
                 :min-size 100
                 :production-rate 8e-3
                 :tolerance {:detrit {:model :limit
                                      :ideal 60
                                      :radius 10}
                             :soil {:model :limit
                                    :ideal 37.9
                                    :radius 0.1}}
                 :influence {:detrit 2.8e-6}}
 :fungus {:size 3e4
                :max-size 1e5
                :min-size 100
                :production-rate 8e-3
                :tolerance {:detrit {:model :limit
                                     :ideal 40
                                     :radius -10}                       
                            :soil {:model :limit
                                   :ideal 30
                                   :radius 10}}
                :influence {:detrit -3.2e-6
                            :soil 0.7e-6}}
 



 :moss {:size 1e4
        :max-size 2e5
        :min-size 100
        :production-rate 1e-2
        :influence {:detrit 2.5e-6}
        :tolerance {:detrit {:model :limit
                             :ideal 90
                             :radius 20}
                    :soil {:model :limit
                           :ideal 99
                           :radius 1}}}
 :worms {:size 6e4
         :max-size 2e5
         :min-size 10
         :production-rate 1.3e-2
         :tolerance {:detrit {:model :limit
                              :ideal 60
                              :radius -17}
                     :soil {:model :limit
                            :ideal 110
                            :radius 15}}
         :influence {:detrit -2.7e-6
                     :soil 2.6e-7}}




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
 :insects {:size 1e4
           :max-size 1e4
           :min-size 2
           :production-rate 0.39e-1
           :tolerance {:detrit {:model :limit
                                :ideal 10
                                :radius -6}}
           :influence {:detrit -13e-4
                       :soil 10e-4}}
 




:bushes {:size 4
          :max-size 1000
          :min-size 2
          :production-rate 1.1e-2
          :tolerance {:soil {:model :limit
                             :ideal 80
                             :radius -10}}
          :influence {:detrit 5e-2
                      :oxygen 0
                      :soil -7e-5}}









 :foliar_trees {:size 1
                :max-size 20
                :min-size 1
                :production-rate 1e-3
                :tolerance {:soil {:model :limit
                                   :ideal 10
                                   :radius -1}}
                :influence {:detrit 0.4
                            :oxygen 2
                            :soil -0.5}}}