(ns payments.db.datomic.payment
  (:require [schema.core :as s]
            [payments.models.payment :as models.payment]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [common-labsoft.time :as time]
            [common-labsoft.datomic.api :as datomic]))

(s/defn lookup! :- models.payment/PaymentStatus
  [payment-id :- s/Uuid
   datomic :- protocols.datomic/IDatomic]
  (datomic/lookup! :payment/id payment-id datomic))

(s/defn insert! :- models.payment/Payment
  [payment :- models.payment/Payment
   datomic :- protocols.datomic/IDatomic]
  (let [prepared-payment (assoc payment :payment/created-at (time/now))]
    (datomic/insert! :payment/id prepared-payment datomic)))

(s/defn update! :- models.payment/Payment
  [payment :- models.payment/Payment
   datomic :- protocols.datomic/IDatomic]
  (datomic/update! :payment/id payment datomic))
