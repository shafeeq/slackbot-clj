(ns test-bot.slack
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [gniazdo.core :as ws]
            [test-bot.slack.io :as io]
            [test-bot.slack.messaging :as messaging]
            [clojure.tools.logging :as log]
            [clj-fuzzy.metrics :refer [dice]]))

(defn configure [url users]
  (log/info "rtm.start ok. ws-url: " url)
  (io/configure url messaging/receiver)
  (messaging/store-users users))

(defn setup []
  (let [{:keys [ok url users] :as response} (io/start-rtm)]
    (if ok
      (configure url users)
      (log/error "rtm.start not ok. Response: " response))))
