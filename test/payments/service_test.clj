(ns payments.service-test
  (:require [midje.sweet :refer :all]
            [payments.service :as ser]
            [common-labsoft.test-helpers :as th]
            [matcher-combinators.midje :refer [match]]
            [matcher-combinators.matchers :as m]
            [common-labsoft.misc :as misc]))

(def card-id (misc/uuid))
(def order-id (misc/uuid))

(th/with-service [ser/start! ser/stop!] [system service]
  (th/with-token {:token/scopes ["jaiminho"]}
    (fact "Http Test"
      (th/request! service :post "/api/payments" {:payment {:amount   50.0M
                                                            :card-id  card-id
                                                            :order-id order-id}})
      => (match {:payment {:amount   50.0
                           :card-id  (str card-id)
                           :id       string?
                           :order-id (str order-id)
                           :status   "authorized"}}))))
