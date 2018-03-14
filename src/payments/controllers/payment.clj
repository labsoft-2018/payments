(ns payments.controllers.payment
  (:require [schema.core :as s]
            [payments.models.payment :as models.payment]
            [payments.db.datomic.payment :as datomic.payment]
            [payments.diplomat.http :as diplomat.http]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [common-labsoft.protocols.http-client :as protocols.http-client]
            [common-labsoft.exception :as exception]
            [common-labsoft.time :as time]))

(s/defn ^:private payment-for-result :- models.payment/Payment
  [payment :- models.payment/Payment
   authorized? :- s/Bool]
  (if authorized?
    (assoc payment :payment/status :payment.status/authorized
                   :payment/authorized-at (time/now))
    (assoc payment :payment/status :payment.status/refused
                   :payment/refused-at (time/now))))

(s/defn ^:private captured? :- s/Bool
  [payment :- models.payment/Payment]
  (= :payment.status/captured (:payment/status payment)))

(s/defn one-payment :- models.payment/Payment
  [payment-id :- s/Uuid
   datomic :- protocols.datomic/IDatomic]
  (datomic.payment/lookup! payment-id datomic))

(s/defn new-payment-request! :- models.payment/Payment
  [payment-request :- models.payment/Payment
   datomic :- protocols.datomic/IDatomic,
   http :- protocols.http-client/IHttpClient]
  (let [payment (datomic.payment/insert! payment-request datomic)
        authorized? (diplomat.http/authorize-payment! payment http)]
    (-> (payment-for-result payment authorized?)
        (datomic.payment/update! datomic))
    (when-not authorized?
      (exception/bad-request! {:payment payment}))))

(s/defn capture-payment!
  [payment-id :- s/Uuid
   datomic :- protocols.datomic/IDatomic
   http :- protocols.http-client/IHttpClient]
  (let [payment (datomic.payment/lookup! payment-id datomic)]
    (when-not (captured? payment)
      (diplomat.http/capture-payment! payment http)
      (-> (assoc payment :payment/status :payment.status/captured
                         :payment/captured-at (time/now))
          (datomic.payment/update! datomic)))))
