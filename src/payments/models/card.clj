(ns payments.models.card
  (:require [schema.core :as s]
            [common-labsoft.time :as time]
            [common-labsoft.schema :as schema]))

(def card-skeleton {:card/id          {:schema s/Uuid :id true}
                    :card/name        {:schema s/Str :required true}
                    :card/number      {:schema s/Str :required true}
                    :card/cvv         {:schema s/Str :required true}
                    :card/exp-date    {:schema s/Str :required true}
                    :card/customer-id {:schema s/Uuid :required true}
                    :card/created-at  {:schema time/LocalDateTime :required true}})
(s/defschema Card (schema/skel->schema card-skeleton))
