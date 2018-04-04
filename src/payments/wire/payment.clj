(ns payments.wire.payment
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema])
  (:import (java.time LocalDateTime)))

(def payment-statuses #{:requested :authorized :captured :refused})
(s/defschema PaymentStatus (apply s/enum payment-statuses))

(def payment-skeleton {:id         {:schema s/Uuid :required true}
                       :amount     {:schema BigDecimal :required true}
                       :order-id   {:schema s/Uuid :required true}
                       :card-id    {:schema s/Uuid :required true}
                       :status     {:schema PaymentStatus :required true}
                       :created-at {:schema LocalDateTime}})
(s/defschema Payment (schema/skel->schema payment-skeleton))

(def payment-document-skeleton {:payment {:schema Payment :required true}})
(s/defschema PaymentDocument (schema/skel->schema payment-document-skeleton))

(def payment-request-skeleton {:payment (select-keys payment-skeleton [:amount :card-id :order-id])})
(s/defschema PaymentRequest (schema/skel->schema payment-request-skeleton))

(def capture-payment-skeleton {:payment (select-keys payment-skeleton [:id])})
(s/defschema CapturePaymentDocument (schema/skel->schema capture-payment-skeleton))
