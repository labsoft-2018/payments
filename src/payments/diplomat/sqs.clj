(ns payments.diplomat.sqs
  (:require [schema.core :as s]
            [payments.wire.payment :as wire.payment]
            [payments.controllers.payment :as controllers.payment]))

(s/defn capture-payment!
  [{payment :payment} :- wire.payment/CapturePaymentDocument, {:keys [http datomic]}]
  (controllers.payment/capture-payment! (:id payment) datomic http))

(def settings {:capture-payment {:handler   capture-payment!
                                 :schema    wire.payment/CapturePaymentDocument
                                 :direction :consumer}})
