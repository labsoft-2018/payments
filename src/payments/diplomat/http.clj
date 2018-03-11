(ns payments.diplomat.http
  (:require [schema.core :as s]
            [payments.models.payment :as models.payment]
            [common-labsoft.protocols.http-client :as protocols.http-client]))

(s/defn authorize-payment! :- s/Bool
  [payment :- models.payment/Payment, http :- protocols.http-client/IHttpClient]
  true)                                                     ;; Mock response from acquirer

(s/defn capture-payment! :- s/Bool
  [payment :- models.payment/Payment, http :- protocols.http-client/IHttpClient]
  true)                                                     ;; Mock response from acquirer
