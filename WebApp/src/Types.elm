module Types exposing (..)

import LngLat exposing (LngLat)

type alias Machine =    { id : Int
                        , coord : LngLat
                        , address : String
                        , description : Maybe String
                        , rating : Maybe Float
                        , reviewsCount : Int
                        , availableTime : Maybe (String, String)
                        }

type alias Review = { rating : Int
                    , comment : Maybe String
                    }

type alias Product = { id : Int
                     , name : String
                     , picture : Maybe String
                     , averagePrice : Maybe Float
                     }

type alias ProductInMachine = { product : Product
                              , isAvailable : Bool
                              , price : Float
                              }