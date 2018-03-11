(ns payments.controllers.payment
  (:require [schema.core :as s]
            [payments.models.payment :as models.payment]
            [payments.db.datomic.payment :as datomic.payment]
            [payments.diplomat.http :as diplomat.http]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [common-labsoft.protocols.http-client :as protocols.http-client]
            [common-labsoft.exception :as exception]))

(s/defn ^:private status-for-result [authorized? :- s/Bool]
  (if authorized?
    :payment.status/authorized
    :payment.status/refused))

(s/defn new-payment-request! :- models.payment/Payment
  [payment-request :- models.payment/Payment
   datomic :- protocols.datomic/IDatomic,
   http :- protocols.http-client/IHttpClient]
  (let [payment (datomic.payment/insert! payment-request datomic)
        authorized? (diplomat.http/authorize-payment! payment http)]
    (-> (assoc payment :payment/status (status-for-result authorized?))
        (datomic.payment/update! datomic))
    (when-not authorized?
      (exception/bad-request! {:payment payment}))))
