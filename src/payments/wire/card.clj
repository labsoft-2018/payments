(ns payments.wire.card
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]
            [common-labsoft.time :as time]))

(def new-card-skeleton {:card {:name        {:schema s/Str :required true}
                               :number      {:schema s/Str :required true}
                               :cvv         {:schema s/Str :required true}
                               :exp-date    {:schema s/Str :required true}
                               :customer-id {:schema s/Uuid :required true}}})
(s/defschema CreateNewCardDocument (schema/skel->schema new-card-skeleton))

(def card-skeleton (-> new-card-skeleton
                       (assoc-in [:card :id] {:schema s/Uuid :required true})
                       (assoc-in [:card :created-at] {:schema time/LocalDateTime :required true})))
(s/defschema CardDocument (schema/skel->schema card-skeleton))
