(ns theladders.bluehornet-client.core
  (:require [clojure.data.xml :as xml]))

(defn make-method [name & {:keys [optional] :as rest}]
  "Create a method representation. Optional arguments are merged with required arguments to create the final argument list."
  (let [required (dissoc rest :optional)]
    {:method-name name :args (into 
                              (into {} optional) 
                              required)}))

(defmulti process-key-value (fn [variable value] (cond (seq? value) :list
                                               (vector? value) :list
                                               (map? value) :map
                                               :else :value)))

(defmethod process-key-value :value [variable value]
  (xml/element variable {} value))

(defmethod process-key-value :list [variable value]
  (let [item-variable (str variable "_item")]
    (apply (partial xml/element variable {}) 
           (for [item value] 
             (process-key-value item-variable item)))))

(defmethod process-key-value :map [variable value] 
  (apply (partial xml/element variable {}) 
         (for [[new-variable new-value] value]
           (process-key-value new-variable new-value))))

(defn method-to-xml [method]
  "Turn a method representation into an xml tree."
  (xml/element :methodCall {}
               (->
                [(xml/element :methodName {} (:method-name method))]
                (into (for [[variable value] (:args method)] (process-key-value variable value))))))

(defn authentication-to-xml [authentication response-type]
  (xml/element :authentication {}
               (into [(xml/element :response_type {} (or response-type "php")) (xml/element :no_halt {} "1")]
               (for [[internal external] {:api-key :api_key
                                          :shared-secret :shared_secret}]
                 (xml/element external {} (internal authentication))))))

(defn make-call-data [authentication methods response-type]
  (xml/element :api {} 
               (authentication-to-xml authentication response-type)
               (xml/element :data {} (for [method methods] (method-to-xml method)))))

(defn call-methods [post-data-fn bhserver authentication methods & {:keys [response-type]}]
  (post-data-fn bhserver (xml/emit-str (make-call-data authentication methods response-type))))
