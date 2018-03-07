(ns payments.controllers.card
  (:require [schema.core :as s]
            [payments.models.card :as models.card]
            [payments.db.datomic.card :as datomic.card]
            [common-labsoft.protocols.datomic :as protocols.datomic]))

(s/defn register-new-card! :- models.card/Card
  [card :- models.card/Card, datomic :- protocols.datomic/IDatomic]
  (datomic.card/insert! card datomic))

(s/defn one-card :- models.card/Card
  [card-id :- s/Uuid, datomic :- protocols.datomic/IDatomic]
  (datomic.card/lookup! card-id datomic))

(s/defn customer-cards :- #{models.card/Card}
  [customer-id :- s/Uuid, datomic :- protocols.datomic/IDatomic]
  (datomic.card/cards-by-customer-id customer-id datomic))
