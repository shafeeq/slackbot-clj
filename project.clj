(defproject test-bot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [clj-http "2.3.0"]
                 [cheshire "5.6.3"]
                 [stylefruits/gniazdo "1.0.0"]
                 [clj-fuzzy "0.1.8"]
                 [clj-time "0.12.0"]
                 [http-kit "2.2.0"]]
  :main ^:skip-aot test-bot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
