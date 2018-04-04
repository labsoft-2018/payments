(ns payments.adapters.card
  (:require [schema.core :as s]
            [payments.wire.card :as wire.card]
            [payments.models.card :as models.card]
            [common-labsoft.misc :as misc]))

(s/defn new-card-wire->internal :- models.card/Card
  [{card :card} :- wire.card/CreateNewCardDocument]
  (misc/map-keys #(keyword "card" (name %)) card))

(s/defn internal->wire-document :- wire.card/CardDocument
  [card :- models.card/Card]
  {:card (misc/map-keys (comp keyword name) card)})

(s/defn internals->wire-documents :- wire.card/CardsDocument
  [cards :- [models.card/Card]]
  {:cards (mapv #(misc/map-keys (comp keyword name) %) cards)})
