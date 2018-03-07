(ns payments.db.datomic.card
  (:require [schema.core :as s]
            [payments.models.card :as models.card]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [common-labsoft.datomic.api :as datomic]
            [common-labsoft.time :as time]))

(s/defn insert! :- models.card/Card
  [card :- models.card/Card, datomic :- protocols.datomic/IDatomic]
  (let [prepared-card (assoc card :card/created-at (time/now))]
    (datomic/insert! :card/id prepared-card datomic)))

(s/defn lookup! :- #{models.card/Card}
  [card-id :- s/Uuid, datomic :- protocols.datomic/IDatomic]
  (datomic/lookup! :card/id card-id (datomic/db datomic)))

(s/defn cards-by-customer-id :- #{models.card/Card}
  [customer-id :- s/Uuid, datomic :- protocols.datomic/IDatomic]
  (datomic/entities '{:find  [?e]
                      :in    [$ ?customer-id]
                      :where [[?e :card/customer-id ?customer-id]]} (datomic/db datomic) customer-id))
