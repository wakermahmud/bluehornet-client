(ns theladders.bluehornet-client.api.legacy
  (:require [theladders.bluehornet-client.core :as core]))

(defn bulk-sync [email template-id & {:as optional}]
  (core/make-method "legacy.bulk_update" :email email :template_id template-id :optional optional))

(defn retrieve-active [& {:as optional}]
  (core/make-method "legacy.retrieve_active" :optional optional))

(defn manage-subscriber [email & {:as optional}]
  (core/make-method "legacy.manage_subscriber" :email email :optional optional))
