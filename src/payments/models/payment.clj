(ns payments.models.payment
  (:require [schema.core :as s]
            [common-labsoft.time :as time]
            [common-labsoft.schema :as schema]))

(def payment-statuses #{:payment.status/created :payment.status/unsettled :payment.status/settled})
(s/defschema PaymentStatus (apply s/enum payment-statuses))

(def payment-skeleton {:payment/id           {:schema s/Uuid :id true}
                       :payment/status       {:schema PaymentStatus :required true}
                       :payment/card-id      {:schema s/Uuid :required true}
                       :payment/amount-cents {:schema s/Int :required true}
                       :payment/created-at   {:schema time/LocalDateTime :required true}
                       :payment/confirmed-at {:schema time/LocalDateTime :required false}
                       :payment/refused-at   {:Schema time/LocalDateTime :required false}})
(s/defschema Payment (schema/skel->schema payment-skeleton))
