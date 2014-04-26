(defproject theladders/bluehornet-client "0.2.1-SNAPSHOT"
  :description "A library for interacting with BlueHornet's API using Clojure."
  :aot :all
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.xml "0.0.7"]]

  :profiles {:repl {:dependencies [[clj-http "0.9.1"]
                                   [cheshire "5.3.1"]]}}

  :plugins [[theladders/lein-uberjar-deploy "0.1.2"]]

  :repositories [["snapshots" {:id "nexus" :url "http://mercurial:8081/nexus/content/repositories/snapshots"}]
                 ["releases"  {:id "nexus" :url "http://mercurial:8081/nexus/content/repositories/releases"}]])
