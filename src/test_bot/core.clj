(ns test-bot.core
  (:require [clojure.string :as string?]
            [cheshire.core :as json]
            [gniazdo.core :as ws]
            [clj-http.client :as client]
            [org.httpkit.server :as server]
            [test-bot.slack.io :as io]
            [test-bot.slack :as slack]
            [test-bot.slack.messaging :as messaging]
            [clojure.tools.logging :as log]))

(defonce http-server (atom nil))

(defn stop-server []
  (when-not (nil? @http-server)
    (@http-server :timeout 100)
    (reset! http-server nil)))

(defn build-response []
  (clojure.pprint/write
   {:socket @io/socket
    :ws-url @io/ws-url
    :token (str (subs io/token 0 10) "...")
    :team-users @messaging/team-users
    :self-username messaging/self-username
    :replied-to @messaging/replied-to
    :msg-id-counter @messaging/msg-id-counter}
   :stream nil))

(defn app [req]
  (log/info req)
  (if (or (= "/force-setup" (:uri req))
          (nil? @io/socket))
    (slack/setup))
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body (build-response)})

(defn -main [& args]
  (let [port 8888]
    (let [ip  "0.0.0.0"]
      (reset! http-server (server/run-server #'app {:ip ip :port port})))))
