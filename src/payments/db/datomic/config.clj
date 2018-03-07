(ns payments.db.datomic.config
  (:require [payments.models.card :as models.card]
            [payments.models.payment :as models.payment]))

(def settings {:schemas [models.card/card-skeleton
                         models.payment/payment-skeleton]
               :enums   [models.payment/payment-statuses]})
