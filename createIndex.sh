#!/usr/bin/env bash

curl -X DELETE  http://$1/earthquakes

curl -H "Content-Type: application/json" -X PUT -d '{
         "mappings": {

           "_default_": {

             "_all": {

               "enabled": false

             },

             "dynamic_templates": [

               {

                 "strings": {

                   "match": "*",

                   "match_mapping_type": "string",

                   "mapping": {

                     "type": "keyword"

                   }

                 }

               }

             ],

             "properties": {

               "@timestamp": {

                 "type": "date",

                 "ignore_malformed": true

               },

               "beat": {

                 "properties": {

                   "hostname": {

                     "type": "keyword"

                   },

                   "name": {

                     "type": "keyword"

                   },

                   "version": {

                     "type": "keyword"

                   }

                 }

               },

               "depth": {

                 "type": "float",

                 "ignore_malformed": true

               },

               "dmin": {

                 "type": "float",

                 "ignore_malformed": true

               },

               "event_id": {

                 "type": "keyword"

               },

               "gap": {

                 "type": "float",

                 "ignore_malformed": true

               },

               "input_type": {

                 "type": "keyword"

               },

               "location": {

                 "type": "geo_point",

                 "ignore_malformed": true

               },

               "mag": {

                 "type": "float",

                 "ignore_malformed": true

               },

               "magType": {

                 "type": "keyword"

               },

               "message": {

                 "type": "keyword"

               },

               "nst": {

                 "type": "keyword"

               },

               "offset": {

                 "type": "long"

               },

               "rms": {

                 "type": "float",

                 "ignore_malformed": true

               },

               "source": {

                 "type": "keyword"

               },

               "type": {

                 "type": "keyword"

               }

             }

           }

         },

         "settings": {

           "index": {

             "refresh_interval": "10s",

             "number_of_shards": "1",

             "number_of_replicas": "0"

           }

         }

                }
' http://$1/earthquakes

curl  -H "Content-Type: application/json" -X POST http://$1/earthquakes/earthquake -d '{"@timestamp": "2016/01/01 03:16:57.64", "location": "-25.3790,-179.4803", "depth": "415.30", "mag": "4.10", "magType": "Mb", "nst": 5, "gap": 71, "dmin": "8", "rms": 1.03, "source": "us", "event_id":201601012009}'
