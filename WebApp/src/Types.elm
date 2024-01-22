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
                    , comment : String
                    }