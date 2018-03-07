(ns payments.wire.card
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]
            [common-labsoft.time :as time]))

(def wire-card {:id          {:schema s/Uuid :required true}
                :name        {:schema s/Str :required true}
                :number      {:schema s/Str :required true}
                :cvv         {:schema s/Str :required true}
                :exp-date    {:schema s/Str :required true}
                :customer-id {:schema s/Uuid :required true}
                :created-at  {:schema time/LocalDateTime :required true}})
(s/defschema WireCard (schema/skel->schema wire-card))

(def new-card-skeleton {:card (dissoc wire-card :id :created-at)})
(s/defschema CreateNewCardDocument (schema/skel->schema new-card-skeleton))

(def card-skeleton {:card {:schema WireCard :required true}})
(s/defschema CardDocument (schema/skel->schema card-skeleton))

(def cards-document {:cards {:schema #{WireCard} :required true}})
(s/defschema CardsDocument (schema/skel->schema cards-document))
