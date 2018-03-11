(ns payments.models.payment
  (:require [schema.core :as s]
            [common-labsoft.time :as time]
            [common-labsoft.schema :as schema]))

(def payment-statuses #{:payment.status/requested
                        :payment.status/authorized
                        :payment.status/captured
                        :payment.status/refused})
(s/defschema PaymentStatus (apply s/enum payment-statuses))

(def payment-skeleton {:payment/id           {:schema s/Uuid :id true}
                       :payment/status       {:schema PaymentStatus :required true}
                       :payment/card-id      {:schema s/Uuid :required true}
                       :payment/order-id     {:schema s/Uuid :required true}
                       :payment/amount       {:schema BigDecimal :required true}
                       :payment/created-at   {:schema time/LocalDateTime :required true}
                       :payment/confirmed-at {:schema time/LocalDateTime :required false}
                       :payment/refused-at   {:schema time/LocalDateTime :required false}})
(s/defschema Payment (schema/skel->schema payment-skeleton))
