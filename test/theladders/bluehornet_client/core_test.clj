(ns theladders.bluehornet-client.core-test
  (:require [clojure.test :refer :all]
            [theladders.bluehornet-client.core :refer :all]
            [clojure.data.xml :as xml]))

(deftest process-key-value-test
  (testing "key-value-value"
    (is (= (process-key-value "name" "Andy") 
           (xml/element "name" {} "Andy"))))
  (testing "key-value-map"
    (is (= (process-key-value "person" {"name" "Andy" "age" "35"}) 
           (xml/element "person" {} (xml/element "name" {} "Andy") (xml/element "age" {} "35")))))
  (testing "key-value-list"
    (is (= (process-key-value "people" [{"name" "Andy"} {"name" "John"}])
           (xml/element "people" {} 
                        (xml/element "people_item" {} (xml/element "name" {} "Andy"))
                        (xml/element "people_item" {} (xml/element "name" {} "John"))))))
)
