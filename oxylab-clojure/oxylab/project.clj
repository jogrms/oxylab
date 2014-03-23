(defproject oxylab "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [enfocus "2.0.2"]
                 [cljspp "0.0.1-SNAPSHOT"]
                 [org.clojure/clojurescript "0.0-2173"]]

  :main ^:skip-aot oxylab.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  
  :source-paths ["src/clj"
                 "target/generated/clj"]

  :plugins [
      [lein-cljsbuild "1.0.2"]
      [com.keminglabs/cljx "0.3.2"]]
  
  :cljx {
         :builds [
                  {
                   :source-paths ["src/cljx"]
                   :output-path "target/generated/clj"
                   :rules :clj}
                  {
                   :source-paths ["src/cljx"]
                   :output-path "target/generated/cljs"
                   :rules :cljs}
                  ]}

  :cljsbuild {
    :builds [{:source-paths ["target/generated/cljs" "src/cljs"]
              :compiler {:output-to "war/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}
  
  :hooks [cljx.hooks])
