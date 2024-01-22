module Machine exposing ( Model
                        , Msg(..)
                        , init
                        , update
                        , view
                        , selectedPointSource
                        , selectedPointLayer
                        , layersFromMachines
                        , listenLayersFromMachines
                        , sourcesFromMachines
                        )

import Html exposing (Html)
import Html.Events
import Json.Decode
import Json.Encode
import Mapbox.Expression as E
import Mapbox.Layer as Layer
import Mapbox.Source as Source
import Styles.Attributes
import LngLat exposing (LngLat)
import InsertMachine
import Http
import Types exposing (Machine)
import Requests exposing (getMachines, postMachine)
import TimePicker exposing (Time)
import SingleSlider exposing (..)
import MachineInfo



--| An event on the map
--| id is String because it is used as name for features

type alias Model = { machines : List Machine
                   , selectedRadius : Maybe Float
                   , selectedPoint : Maybe LngLat
                   , rangeSlider : SingleSlider.SingleSlider Msg
                   , insertModel : Maybe InsertMachine.Model
                   , infoModel : Maybe MachineInfo.Model
                   }

type Msg = RequestedMachines
         | GotMachines (Result Http.Error (List Machine))
         | GotMachine (Result Http.Error Machine)
         | RangeChanged Float
         | MapClicked (String, String, LngLat)
         | MachineInfoMsg MachineInfo.Msg
         | InsertModeEntered
         | InsertMachineMsg InsertMachine.Msg


init : Model
init = 
    let
        nullFormatter =
            \_ -> ""
        
        valueFormatter =
            \fst _ -> "0 - " ++ String.fromFloat fst ++ " m"

    in
        { machines = []
        , selectedRadius = Nothing
        , selectedPoint = Nothing
        , rangeSlider = SingleSlider.init
                            { min = 100
                            , max = 10000
                            , value = 10000
                            , step = 50
                            , onChange = RangeChanged
                            }
                            |> SingleSlider.withMinFormatter nullFormatter
                            |> SingleSlider.withMaxFormatter nullFormatter
                            |> SingleSlider.withValueFormatter valueFormatter
        , insertModel = Nothing
        , infoModel = Nothing
        }


update : (Msg -> msg) -> Msg -> Model -> (Model, Cmd msg)
update wrapMsg msg model = 
    let
        selectedArea = Maybe.map2 (\point radius -> (point, radius)) model.selectedPoint model.selectedRadius
    in
    case msg of
        RequestedMachines ->
            ( { model | infoModel = Nothing, insertModel = Nothing }, getMachines selectedArea (wrapMsg << GotMachines) )

        GotMachines (Ok machines) ->
            ( { model | machines = machines }, Cmd.none )
        
        GotMachines (Err _) ->
            ( model, Cmd.none )

        GotMachine (Ok machineInfo) ->
            let
                newInfoModel = MachineInfo.init machineInfo
            in
            ( { model | infoModel = Just newInfoModel }, getMachines selectedArea (wrapMsg << GotMachines) )

        GotMachine (Err _) ->
            ( { model | infoModel = Nothing }, Cmd.none )

        RangeChanged radius ->
            let
                newSlider = SingleSlider.update radius model.rangeSlider

            in
            ( { model | rangeSlider = newSlider, selectedRadius = Just radius }, Cmd.none)

        MapClicked (featType, featName, lngLat) -> 
                            case ( featType, String.toInt featName ) of
                                ( "machine", Just id ) ->
                                    ( { model | insertModel = Nothing }, Requests.getMachine id (wrapMsg << GotMachine))
                                _ ->
                                    case model.insertModel of
                                        Just insertModel ->
                                            ( { model | insertModel = (
                                                Just <|
                                                InsertMachine.update
                                                    (InsertMachine.PointChoosed (Just lngLat))
                                                    insertModel
                                            )}, Cmd.none )

                                        Nothing ->
                                                    if model.infoModel == Nothing then
                                                        ( { model | selectedPoint = Just lngLat }, Cmd.none )
                                                    else
                                                        ( { model | infoModel = Nothing }, Cmd.none )

        MachineInfoMsg infoMsg ->
            if infoMsg == MachineInfo.MachineInfoClosed then
                ( { model | infoModel = Nothing }, Cmd.none )
            else
            case model.infoModel of

                Just infoModel ->
                    let
                        ( newInfoModel, cmd ) = MachineInfo.update (wrapMsg << MachineInfoMsg) infoMsg infoModel
                    in
                    ( { model | infoModel = Just newInfoModel }, cmd )

                Nothing ->
                    ( model, Cmd.none )

        InsertModeEntered ->
            ( { model | infoModel = Nothing, insertModel = Just InsertMachine.init }, Cmd.none )

        InsertMachineMsg insertMsg ->
            case insertMsg of
                InsertMachine.InsertCancelled ->
                    ( { model | insertModel = Nothing }, Cmd.none )

                InsertMachine.InsertSubmitted ->
                    case model.insertModel of
                        Just insertModel ->
                            case ( insertModel.insertedPoint
                                 , insertModel.insertedAddress ) of
                                ( Just point, Just address ) ->
                                    let timePeriod =
                                            case ( insertModel.insertedTimeFrom
                                                 , insertModel.insertedTimeTo ) of
                                                ( Just from, Just to ) ->
                                                    Just <| timePeriodToString (from, to)

                                                _ ->
                                                    Nothing
                                        machine = Machine -1 point address insertModel.insertedDetails Nothing 0 timePeriod
                                    in
                                    ( { model | insertModel = Nothing }, postMachine machine (wrapMsg << GotMachine)
                                    )
                                _ ->
                                    ( model, Cmd.none )

                        Nothing ->
                            ( model, Cmd.none )

                _ ->
                    case model.insertModel of
                        Just insertModel ->
                            let
                                newInsertModel =
                                    InsertMachine.update insertMsg insertModel
                            in
                            ( { model | insertModel = Just newInsertModel }, Cmd.none )

                        Nothing ->
                            ( model, Cmd.none )

timePeriodToString : (Time, Time) -> (String, String)
timePeriodToString (from, to) =
    let addZero n = if n < 10 then "0" ++ (String.fromInt n) else String.fromInt n
        fromString = addZero from.hours ++ ":" ++ addZero from.minutes
        toString = addZero to.hours ++ ":" ++ addZero to.minutes
    in
    (fromString, toString)


selectedPointSource : Model -> List Source.Source
selectedPointSource model =
    let
        pointToFeature maybePoint =
            case maybePoint of
                Just point ->
                    """
                    {
                    "type": "Feature",
                    "properties": {
                        "name": "selectedPoint"
                    },
                    "geometry": {
                        "type": "Point",
                        "coordinates": [""" ++ String.fromFloat point.lng ++ ", " ++ String.fromFloat point.lat ++ """]
                    }
                    }
                    """
                Nothing ->
                    ""
    in
    Json.Decode.decodeString Json.Decode.value ("""
        {
          "type": "FeatureCollection",
          "features": [
              """ ++ pointToFeature model.selectedPoint ++ """
           ]
        }
        """) |> Result.withDefault (Json.Encode.object [])
        |> Source.geoJSONFromValue "selectedPoint" [ Source.generateIds ]
        |> List.singleton
    


selectedPointLayer : Model -> List Layer.Layer
selectedPointLayer model =
    case model.selectedPoint of
        Just _ ->
            [ Layer.circle "selectedPoint" "selectedPoint"
                [ Layer.circleRadius (E.float 5)
                , Layer.circleColor (E.rgba 255 0 0 255)
                , Layer.circleStrokeColor (E.rgba 0 0 0 255)
                , Layer.circleStrokeWidth (E.float 1)
                ]
            ]

        Nothing ->
            []


--| Information about an event
--| Create a list of sources from a list of events
--| Generates from GeoJSON
--| The sources are used to create layers


sourcesFromMachines : Model -> List Source.Source
sourcesFromMachines model =
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
    List.map machineToFeature model.machines
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


layersFromMachines : Model -> List Layer.Layer
layersFromMachines model =
    List.map
        (\machine ->
            Layer.circle ("machine." ++ String.fromInt machine.id)
                "machines"
                [ Layer.circleRadius (E.float 10)
                , Layer.circleColor (E.ifElse (E.toBool (E.featureState (E.str "hover")))
                    (E.rgba 100 255 100 255)
                    (E.rgba 0 150 255 255)
                )
                , Layer.circleStrokeColor (E.rgba 0 0 0 255)
                , Layer.circleStrokeWidth (E.float 1)
                , Layer.circleOpacity (E.float 0.5)
                ]
        )
        model.machines



--| Create a list of layer names from a list of events


listenLayersFromMachines : Model -> List String
listenLayersFromMachines model =
    List.map (\machine -> "machine." ++ String.fromInt machine.id) model.machines



--| View for the event info


view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
            let 
                insertButton =
                    if model.insertModel == Nothing then
                        Html.button
                            (Styles.Attributes.insertButton
                                ++ [ Html.Events.onClick (wrapMsg InsertModeEntered) ]
                            )
                            [ Html.text "Add Machine" ]
                    else
                        Html.div [] []

                insertForm =
                    case model.insertModel of
                        Just insertModel ->
                            InsertMachine.view (wrapMsg << InsertMachineMsg) insertModel

                        Nothing ->
                            Html.div [] []
                
                rangeSlider =
                    Html.div Styles.Attributes.rangeSlider
                        [ Html.div [] [ Html.text "Radius: " ]
                        , Html.map wrapMsg <| SingleSlider.view model.rangeSlider
                        ]

                refreshButton =
                    Html.button
                        (Styles.Attributes.refreshButton
                            ++ [ Html.Events.onClick (wrapMsg RequestedMachines) ]
                        )
                        [ Html.text "Refresh" ]

            in
    case model.infoModel of
        Nothing ->
            case model.insertModel of
                Just _ ->
                    Html.div [] [ insertForm, insertButton ]

                Nothing -> Html.div [] [ rangeSlider, refreshButton, insertButton ]

        Just info -> MachineInfo.view (wrapMsg << MachineInfoMsg) info
