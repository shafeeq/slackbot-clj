(ns test-bot.slack.messaging
  (:require [test-bot.slack.io :as io]
            [clojure.string :as string]
            [clj-fuzzy.metrics :refer [dice]]
            [clojure.tools.logging :as log]
            [clj-time.core :as time]))

(def team-users (atom nil))
(def self-username "testbot")
(def msg-id-counter (atom 0))
(def replied-to (atom {}))

(def salutations
  ["Howdy, " "Hi, " "Hello, " "Hey, "])

(def greetings
  ["Good morning!" "Morning!"
   "G'Morning!" "Top O' the mornin' to ya!"])

(def emojis
  [":sunny:" ":sunrise_over_mountains:"
   ":sun_with_face:" ":city_sunrise:"])

(defn morning-greeting [username]
  (let [[salutation greeting emoji] (map rand-nth [salutations greetings emojis])]
    (string/join " "
                 [(str salutation username "! ") greeting emoji])))

(defn store-users [users]
  (reset! team-users
          (->> (for [user users]
                 {(:id user) (:name user)})
               (into {}))))

(defn send-reply [channel user text]
  (let [today (str (time/today))
        username (get @team-users user)]
    (if (nil? (get @replied-to today))
      (swap! replied-to assoc today #{}))

    (if (> (dice "good morning" text ) 0.5)
      (when-not (contains? (get @replied-to today) username)
        (io/send-over-ws {:id (swap! msg-id-counter inc)
                          :channel channel
                          :type "message"
                          :text (morning-greeting username)})
        (swap! replied-to update today #(conj % username))))))

(defn receiver [{:keys [type channel user text] :as message}]
  (log/info "Got msg: " message)
  (if (= type "message")
    (if (not= (get @team-users user) self-username)
      (send-reply channel user text))))
