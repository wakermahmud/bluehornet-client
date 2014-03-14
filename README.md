# bluehornet-client

A library for interacting with BlueHornet's API using Clojure.

## Installation
Add the following dependency to your project.clj file:

[theladders/bluehornet-client "0.1.4"]

## Usage

bluehornet-client allows a program to make calls to the BlueHornet
API. Creating API calls is separated from making the calls because the
API allows calls to be sent in groups. The API calls are created by
the functions in the namespaces under
`theladders.bluehornet-client.api`, while making the call is done by
`theladders.bluehornet-client.core/call-methods`.

The first argument to the `call-methods` function is a function that can POST data to a URL. Here's an example of such a function, created with the `clj-http` library:

```
(ns bluehornet-client-example
  (:require [clj-http.client :as client]))

(defn post-fn [url data] 
  (client/post url {:body data}))
```

The second argument is the URL for the BlueHornet API. This should be
set to `https://echo.bluehornet.com/api/xmlrpc/index.php` in
production, but in testing it may be helpful to point it to a local
HTTP server.

The third argument is an authentication map, which must have
`:api-key` (the BlueHornet API key) and `:shared-secret` (the
BlueHornet shared secret) keys. Additionally, it may contain a key
`:response-type` that specifies the format of the response and can
contain the values "json", "php", or "xml" (the default is "php"). For
example:

```
{:api-key "BLUEHORNET_API_KEY"
 :shared-secret "BLUEHORNET_SHARED_SECRET"
 :response-type "json"}
```

The fourth argument is the list of method objects created by calls to
functions in `theladders.bluehornet-client.api.*`.

The following example will add or update a subscriber in BlueHornet,
and add them to the 1337 group:

```
(ns bluehornet-client-example
  (:require [theladders.bluehornet-client.core :as core]
            [theladders.bluehornet-client.legacy :as legacy]
            [clj-http.client :as client]))

(defn post-fn [url data]
  (client/post url {:body data}))

(def api-key "BLUEHORNET_API_KEY")
(def shared-secret "BLUEHORNET_SHARED_SECRET")

(def authentication {:api-key api-key
 :shared-secret shared-secret
 :response-type "json"})

(def bluehornet-api-url
     "https://echo.bluehornet.com/api/xmlrpc/index.php")

(def manage-subscriber-call
     (manage-subscriber "aturley@theladders.com" :grp "1337"))

(core/call-methods 
  post-fn 
  bluehornet-api-url
  authentication 
  [manage-subscriber-call])
```

## Design and Conventions

The goal of the library is to make it easy to call the BlueHornet API
from Clojure programs, and to make it easy to grow the library as
needed. The only direct dependency is org.clojure/data.xml, which is
used to generate the XML that is ultimately sent to BlueHornet.

### Namespace Layout

The functions that correspond to API methods are in namespaces under
`theladders.bluehornet-client.api`. For example, the BlueHornet API
method `legacy.manage_subscriber` is implemented by a function called
`manage-subscriber` in `theladders.bluehornet-client.api.legacy`. New
API methods should be added to the appropriate namespace, and new
namespaces should be added as needed if they do not exist.

### Function Conventions

By convention, each API method is implemented as a function in the
appropriate namespace. The fields that are listed as "required" in
the BlueHornet API documention are implemented as positional
parameters in the bluehornet-client functions. Fields that are not
"required" are passed as optional keys/values.

For example, the `legacy.manage_subscriber` API method has one
required argument, `email`, and a number of optional parameters. The
function definition looks like this:

    (defn manage-subscriber [email & {:as optional}] ...)

The function is called like this:

    (manage-subscriber "aturley@theladders.com" :grp "1337")

In the above example, the subscriber "aturley@theladders.com" is added
to the 1337 group. The optional parameter keys may be either strings
or Clojure keywords, and they must exactly match the field names
specified in the BlueHornet API documentation.

## License

Copyright © 2013 TheLadders, Inc

Distributed under the MIT public license.
