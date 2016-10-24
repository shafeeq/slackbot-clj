(ns test-bot.slack.io
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [gniazdo.core :as ws]
            [clojure.walk :as walk]
            [clojure.tools.logging :as log]))

(def socket (atom nil))
(def ws-url (atom nil))
(def token "<bot-auth-token-here>")

(defn configure [url message-receiver]
  (reset! ws-url url)
  (reset! socket
          (ws/connect
           @ws-url
           :on-receive (fn [message]
                         (-> message
                             json/decode
                             walk/keywordize-keys
                             message-receiver)))))

(defn start-rtm []
  (-> (client/post "https://slack.com/api/rtm.start"
                   {:form-params {:token token}})
      :body
      json/decode
      walk/keywordize-keys))

(defn send-over-ws [data]
  (ws/send-msg @socket (json/encode data)))
