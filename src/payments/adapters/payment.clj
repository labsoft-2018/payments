(ns payments.adapters.payment
  (:require [schema.core :as s]
            [payments.models.payment :as models.payment]
            [payments.wire.payment :as wire.payment]
            [common-labsoft.misc :as misc]))

(s/defn payment-request->internal :- models.payment/Payment
  [{payment-request :payment} :- wire.payment/PaymentRequest]
  {:payment/amount   (:amount payment-request)
   :payment/order-id (:order-id payment-request)
   :payment/card-id  (:card-id payment-request)
   :payment/status   :payment.status/requested})

(s/defn internal->wire :- wire.payment/Payment
  [payment :- models.payment/Payment]
  (-> (misc/map-keys (comp keyword name) payment)
      (update :status (comp keyword name))))

(s/defn internal->document :- wire.payment/PaymentDocument
  [payment :- models.payment/Payment]
  {:payment (internal->wire payment)})
