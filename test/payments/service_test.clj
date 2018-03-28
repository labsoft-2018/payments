(ns payments.service-test
  (:require [midje.sweet :refer :all]
            [payments.service :as ser]
            [common-labsoft.test-helpers :as th]
            [matcher-combinators.midje :refer [match]]
            [common-labsoft.misc :as misc]))

(def card-id (misc/uuid))
(def order-id (misc/uuid))
(def customer-id (misc/uuid))

(th/with-service [ser/start! ser/stop!] [system service]
  (fact "Creates a new card"
    (th/request! service :post "/api/cards" {:card {:name        "test"
                                                    :number      "1234"
                                                    :cvv         "123"
                                                    :exp-date    #time/date "2020-10-10"
                                                    :customer-id customer-id}})
    => (match {:card {:id          string?
                      :name        "test"
                      :number      "1234"
                      :cvv         "123"
                      :exp-date    "2020-10-10"
                      :customer-id (str customer-id)}}))

  (th/with-token {:token/scopes ["jaiminho"]}
    (fact "Creates a new payment request"
      (th/request! service :post "/api/payments" {:payment {:amount   50.0M
                                                            :card-id  card-id
                                                            :order-id order-id}})
      => (match {:payment {:amount   50.0
                           :card-id  (str card-id)
                           :id       string?
                           :order-id (str order-id)
                           :status   "authorized"}}))))
