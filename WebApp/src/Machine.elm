module Machine exposing ( Machine
                        , Review
                        , layersFromMachines
                        , listenLayersFromMachines
                        , sourcesFromMachines
                        , viewMachineInfo)

import Html exposing (Html)
import Html.Events
import Json.Decode
import Json.Encode
import Mapbox.Expression as E
import Mapbox.Layer as Layer
import Mapbox.Source as Source
import Styles.Attributes
import LngLat exposing (LngLat)



--| An event on the map
--| id is String because it is used as name for features


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


--| Information about an event
--| Create a list of sources from a list of events
--| Generates from GeoJSON
--| The sources are used to create layers


sourcesFromMachines : List Machine -> List Source.Source
sourcesFromMachines machines =
    let
        coords machine =
            "[" ++ String.fromFloat machine.coord.lng ++ ", " ++ String.fromFloat machine.coord.lat ++ "]"

        machineToFeature machine =
            """
            {
              "type": "Feature",
              "properties": {
                "name": "machine.""" ++ String.fromInt machine.id ++ """"
              },
              "geometry": {
                "type": "Point",
                "coordinates": """ ++ coords machine ++ """
              }
            }
            """
    in
    List.map machineToFeature machines
        |> String.join ","
        |> (\features -> Json.Decode.decodeString Json.Decode.value ("""
        {
          "type": "FeatureCollection",
          "features": [
              """ ++ features ++ """
           ]
        }
        """) |> Result.withDefault (Json.Encode.object []))
        |> Source.geoJSONFromValue "machines" [ Source.generateIds ]
        |> List.singleton



--| Create a list of layers from a list of events
--| creates a circle layer for each event
--| hover effect by changing opacity


layersFromMachines : List Machine -> List Layer.Layer
layersFromMachines machines =
    List.map
        (\machine ->
            Layer.circle ("machine." ++ String.fromInt machine.id)
                "machines"
                [ Layer.circleRadius (E.float 10)
                , Layer.circleColor (E.rgba 0 150 255 255)
                , Layer.circleStrokeColor (E.rgba 0 0 0 255)
                , Layer.circleStrokeWidth (E.float 1)
                , Layer.circleOpacity
                    (E.ifElse (E.toBool (E.featureState (E.str "hover")))
                        (E.float 0.7)
                        (E.float 0.2)
                    )
                ]
        )
        machines



--| Create a list of layer names from a list of events


listenLayersFromMachines : List Machine -> List String
listenLayersFromMachines machines =
    List.map (\machine -> "machine." ++ String.fromInt machine.id) machines



--| View for the event info


viewMachineInfo : msg -> Maybe Machine -> Html msg
viewMachineInfo mapMsg machineInfo =
    case machineInfo of
        Nothing ->
            Html.div [] []

        Just info ->
            Html.div Styles.Attributes.eventInfo
                [ Html.h2 [] [ Html.text info.address ]
                , Maybe.withDefault (Html.div [] []) 
                    <| Maybe.map (\description -> Html.p [] [ Html.text description ]) info.description
                
                , Html.button
                    (Styles.Attributes.closeButton
                        ++ [ Html.Events.onClick mapMsg ]
                    )
                    [ Html.text "X" ]
                ]
