(ns theladders.bluehornet-client.api.transactional
  (:require [theladders.bluehornet-client.core :as core]))

(defn send-transaction [email template-id & {:as optional}]
  (core/make-method "transactional.sendTransaction" :email email :template_id template-id :optional optional))

(defn update-template [template-id & {:as optional}]
  (core/make-method "transactional.updateTemplate" :template_id template-id :optional optional))
