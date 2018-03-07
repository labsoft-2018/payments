(ns payments.routes
  (:require [common-labsoft.pedestal.interceptors.auth :as int-auth]
            [common-labsoft.pedestal.interceptors.error :as int-err]
            [common-labsoft.pedestal.interceptors.adapt :as int-adapt]
            [common-labsoft.pedestal.interceptors.schema :as int-schema]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http.body-params :as body-params]
            [payments.controllers.card :as controllers.card]
            [payments.adapters.card :as adapters.card]
            [payments.wire.card :as wire.card]))

(defn one-card
  [{{:keys [datomic]} :components card-id :card-id}]
  {:status 200
   :body   (-> (controllers.card/one-card card-id datomic)
               (adapters.card/internal->wire-document))})

(defn register-new-card
  [{{:keys [datomic]} :components new-card :data}]
  {:status 200
   :schema wire.card/CardDocument
   :body   (-> (adapters.card/new-card-wire->internal new-card)
               (controllers.card/register-new-card! datomic)
               (adapters.card/internal->wire-document))})

(defn customer-cards
  [{{:keys [datomic]} :components customer-id :customer-id}]
  {:status 200
   :schema wire.card/CardsDocument
   :body   (-> (controllers.card/customer-cards customer-id datomic)
               (adapters.card/internals->wire-documents))})

(defroutes routes
  [[["/" ^:interceptors [int-err/catch!
                         (body-params/body-params)
                         int-adapt/coerce-body
                         int-adapt/content-neg-intc
                         int-auth/auth
                         int-schema/coerce-output]

     ["/customers/:id" ^:interceptors [(int-adapt/path->uuid :id :customer-id)]
      ["/cards" {:get [:customer-cards customer-cards]}]]

     ["/cards"
      {:post [:register-new-card ^:interceptors [(int-schema/coerce wire.card/CreateNewCardDocument)]
              register-new-card]}

      ["/:id" ^:interceptors [(int-adapt/path->uuid :id :card-id)]
       {:get [:one-card one-card]}]]]]])
