(ns payments.service-test
  (:require [midje.sweet :refer :all]
            [payments.service :as ser]
            [common-labsoft.test-helpers :as th]
            [matcher-combinators.midje :refer [match]]
            [common-labsoft.misc :as misc]
            [payments.diplomat.sqs :as sqs]))

(def card-id (misc/uuid))
(def order-id (misc/uuid))
(def customer-id (misc/uuid))
(def card-payload {:card {:name        "test"
                          :number      "1234"
                          :cvv         "123"
                          :exp-date    #time/date "2020-10-10"
                          :customer-id customer-id}})

(defn create-card [world]
  (th/with-token {:token/scopes ["jaiminho"]}
    (th/request! world :post "/api/cards" card-payload)))

(defn customer-cards [world]
  (th/with-token {:token/scopes ["admin"]}
    (th/request! world :get (str "/api/customers/" customer-id "/cards"))))

(defn create-payment [world]
  (->> (th/with-token {:token/scopes ["jaiminho"]}
         (th/request! world :post "/api/payments" {:payment {:amount   50.0M
                                                             :card-id  card-id
                                                             :order-id order-id}}))
       (th/assoc-to-world world :payment)))

(defn one-payment [world]
  (th/with-token {:token/scopes ["jaiminho"]}
    (th/request! world :get (str "/api/payments/" (th/get-in-world world [:payment :payment :id])))))

(defn capture-payment [world]
  (sqs/capture-payment! {:payment {:id (misc/str->uuid (th/get-in-world world [:payment :payment :id]))}} (th/get-system world)))

(th/with-world [world ser/restart!]
  (fact "Creates a new card"
    (create-card world) => (match {:card {:id          string?
                                          :name        "test"
                                          :number      "1234"
                                          :cvv         "123"
                                          :exp-date    "2020-10-10"
                                          :customer-id (str customer-id)}}))

  (fact "Get all customer cards"
    (customer-cards world) => (match {:cards [{:customer-id (str customer-id)
                                               :cvv         "123"
                                               :exp-date    "2020-10-10"
                                               :name        "test"
                                               :number      "1234"}]}))

  (create-payment world)
  (fact "Creates a new payment request"
    (th/get-in-world world [:payment]) => (match {:payment {:amount   50.0
                                                            :card-id  (str card-id)
                                                            :id       string?
                                                            :order-id (str order-id)
                                                            :status   "authorized"}}))

  (fact "we can retrieve this payment"
    (one-payment world) => (match {:payment {:amount   50.0
                                             :card-id  (str card-id)
                                             :id       string?
                                             :order-id (str order-id)
                                             :status   "authorized"}}))

  (capture-payment world)
  (fact "the payment status is updated"
    (one-payment world) => (match {:payment {:amount   50.0
                                             :card-id  (str card-id)
                                             :id       string?
                                             :order-id (str order-id)
                                             :status   "captured"}})))
