module Requests exposing ( getMachines
                         , getMachine
                         , postMachine
                         , getReviews
                         , postReview
                         , getAbout
                         )

import Http
import Json.Decode
import Json.Encode
import LngLat exposing (LngLat)
import Machine exposing (Machine, Review)
import Time exposing (Month(..))

getMachines : Maybe { center : LngLat, radius : Float } -> (Result Http.Error (List Machine) -> msg) -> Cmd msg
getMachines radius msg =
    let
        url =
            case radius of
                Just r ->
                    "api/machines?latitude=" ++
                    String.fromFloat r.center.lat ++ 
                    "&longitude=" ++ String.fromFloat r.center.lng ++ 
                    "&radius=" ++ String.fromFloat r.radius

                Nothing ->
                    "api/machines"
    in
    Http.request
        { method = "GET"
        , headers = []
        , url = url
        , body = Http.emptyBody
        , expect = Http.expectJson msg (Json.Decode.list machineDecoder)
        , timeout = Just 10000
        , tracker = Nothing
        }

getMachine : Int -> (Result Http.Error Machine -> msg) -> Cmd msg
getMachine id msg =
    Http.request
        { method = "GET"
        , headers = []
        , url = "api/machines/" ++ String.fromInt id
        , body = Http.emptyBody
        , expect = Http.expectJson msg machineDecoder
        , timeout = Just 10000
        , tracker = Nothing
        }

postMachine : Machine -> (Result Http.Error Machine -> msg) -> Cmd msg
postMachine machine msg =
    Http.request
        { method = "POST"
        , headers = []
        , url = "api/machines"
        , body = Http.jsonBody <| machineEncoder machine
        , expect = Http.expectJson msg machineDecoder
        , timeout = Just 10000
        , tracker = Nothing
        }

getReviews : Int -> (Result Http.Error (List Review) -> msg) -> Cmd msg
getReviews machineId msg =
    Http.request
        { method = "GET"
        , headers = []
        , url = "api/machines/" ++ String.fromInt machineId ++ "/reviews"
        , body = Http.emptyBody
        , expect = Http.expectJson msg (Json.Decode.list reviewDecoder)
        , timeout = Just 10000
        , tracker = Nothing
        }

postReview : Int -> Review -> (Result Http.Error Review -> msg) -> Cmd msg
postReview machineId review msg =
    Http.request
        { method = "POST"
        , headers = []
        , url = "api/machines/" ++ String.fromInt machineId ++ "/reviews"
        , body = Http.jsonBody <| reviewEncoder review
        , expect = Http.expectJson msg reviewDecoder
        , timeout = Just 10000
        , tracker = Nothing
        }

nullable : (a -> Json.Encode.Value) -> Maybe a -> Json.Encode.Value
nullable encoder value =
    Maybe.withDefault Json.Encode.null (Maybe.map encoder value)
    
machineDecoder : Json.Decode.Decoder Machine
machineDecoder =
    Json.Decode.map7 (\id coord address description rating reviewsCount availableTime -> 
        { id = id
        , coord = coord
        , address = address
        , description = description
        , rating = rating
        , reviewsCount = reviewsCount
        , availableTime = availableTime
        })
        (Json.Decode.field "machineId" Json.Decode.int)
        (Json.Decode.field "coordinates" lngLatDecoder)
        (Json.Decode.field "address" Json.Decode.string)
        (Json.Decode.field "description" (Json.Decode.nullable Json.Decode.string))
        (Json.Decode.field "rating" (Json.Decode.nullable Json.Decode.float))
        (Json.Decode.field "reviewsCount" Json.Decode.int)
        (Json.Decode.field "availableTime" (Json.Decode.nullable timePeriodDecoder))

machineEncoder : Machine -> Json.Encode.Value
machineEncoder machine =
    Json.Encode.object
        [ ( "address", Json.Encode.string machine.address )
        , ( "description", nullable Json.Encode.string machine.description )
        , ( "coordinates", coordinatesEncoder machine.coord )
        , ( "availableTime", nullable availableTimeEncoder machine.availableTime )
        ]

lngLatDecoder : Json.Decode.Decoder LngLat
lngLatDecoder =
    Json.Decode.map2 LngLat
        (Json.Decode.field "longitude" Json.Decode.float)
        (Json.Decode.field "latitude" Json.Decode.float)

coordinatesEncoder : LngLat -> Json.Encode.Value
coordinatesEncoder coord =
    Json.Encode.object
        [ ( "latitude", Json.Encode.float coord.lat )
        , ( "longitude", Json.Encode.float coord.lng )
        ]

timePeriodDecoder : Json.Decode.Decoder (String, String)
timePeriodDecoder =
    Json.Decode.map2 (\from to -> (from, to))
        (Json.Decode.field "availableFrom" Json.Decode.string)
        (Json.Decode.field "availableTo" Json.Decode.string)

availableTimeEncoder : (String, String) -> Json.Encode.Value
availableTimeEncoder (from, to) =
    Json.Encode.object
        [ ( "availableFrom", Json.Encode.string from )
        , ( "availableTo", Json.Encode.string to )
        ]

reviewDecoder : Json.Decode.Decoder Review
reviewDecoder =
    Json.Decode.map2 (\rating comment -> 
        { rating = rating
        , comment = comment
        })
        (Json.Decode.field "rating" Json.Decode.int)
        (Json.Decode.field "comment" Json.Decode.string)

reviewEncoder : Review -> Json.Encode.Value
reviewEncoder review =
    Json.Encode.object
        [ ( "rating", Json.Encode.int review.rating )
        , ( "comment", Json.Encode.string review.comment )
        ]


getAbout : (Result Http.Error String -> msg) -> Cmd msg
getAbout msg =
    Http.request
        { method = "GET"
        , headers = []
        , url = "static/about.txt"
        , body = Http.emptyBody
        , expect = Http.expectString msg
        , timeout = Just 10000
        , tracker = Nothing
        }