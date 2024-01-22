module Map exposing (Mode(..), Model, Msg(..), init, update, view)

import Machine exposing (..)
import Html exposing (Html, div, text)
import Html.Events
import Http
import Json.Decode
import Json.Encode
import LngLat exposing (LngLat)
import Mapbox.Element exposing (..)
import Mapbox.Source as Source
import Mapbox.Style as Style exposing (Style(..))
import Requests
import Styles.Attributes
import Styles.Streets exposing (styleLayers)

import InsertMachine
import TimePicker exposing (Time)


{-| The model of the map

  - hoveredFeatures: the features currently hovered by the mouse

  - regions: { name, geometry } to be displayed on the map

  - selectedRegion: the region currently selected

  - events: { id, geometry } to be displayed on the map

  - selectedEvent: the event currently selected

  - insertion messages to insert a new event

  - about: the about text to be displayed

-}
type alias Model =
    { hoveredFeatures : List Json.Encode.Value
    , mode : Mode
    , machines : List Machine
    , selectedMachine : Maybe Machine
    , insertModel : Maybe InsertMachine.Model
    , about : String
    }


--| Mode that map is currently working in
--| Mode is needed to change layers and corrsponding sources on the map without reloading it


type Mode
    = Loading
    | Machines
    | Products
    | About


type Msg
    = Hover EventData
    | MovedOut EventData
    | Click EventData
    | SetMode Mode
    | GotMachines (Result Http.Error (List Machine))
    | GotMachine (Result Http.Error Machine)
    | MachineInfoClosed
    | InsertModeEntered
    | InsertMachineMsg InsertMachine.Msg
    | GotAbout (Result Http.Error String)



--| Initialize the map
--| Get regions, events and about text from the server


init : Model
init =
    { hoveredFeatures = []
    , mode = Loading
    , machines = []
    , selectedMachine = Nothing
    , insertModel = Nothing
    , about = "Loading..."
    }
    



--| get a name of a mapbox feature


featureName : Json.Decode.Decoder String
featureName =
    Json.Decode.at [ "properties", "name" ] Json.Decode.string


update : (Msg -> msg) -> Msg -> Model -> ( Model, Cmd msg )
update wrapMsg msg model =
    case msg of
        Hover { renderedFeatures } ->
            ( { model | hoveredFeatures = renderedFeatures }, Cmd.none )

        MovedOut _ ->
            ( { model | hoveredFeatures = [] }, Cmd.none )

        --| If the map is in insert mode, clicking on the map will start inserting a new event
        --| If the map is not in insert mode, clicking on the map will get the feature name and
        --| request the corresponding info from the server

        Click { lngLat, renderedFeatures } ->
            let
                feature =
                    renderedFeatures
                        |> List.head
                        |> Maybe.withDefault Json.Encode.null
                        |> Json.Decode.decodeValue featureName
                        |> Result.withDefault ""
                        |> String.split "."

                ( featType, featName ) =
                    case feature of
                        [ a, b ] ->
                            ( a, b )

                        _ ->
                            ( "", "" )

                ( newModel,  cmd ) =
                    case model.mode of
                        Machines ->
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
                                            ( { model | selectedMachine = Nothing }, Cmd.none )

                        _ ->
                            ( model, Cmd.none)
            in
            ( newModel, cmd )

        SetMode mode ->
            let
                ( newModel, cmd ) =
                    case mode of
                        Products -> ( model, Cmd.none )

                        Machines ->
                            ( model, Requests.getMachines Nothing (wrapMsg << GotMachines) )

                        About ->
                            ( { model | about = "Loading..."
                            }, Requests.getAbout (wrapMsg << GotAbout) )
                        
                        _ ->
                            ( model, Cmd.none )
            in
            ( { newModel | mode = mode
                         , hoveredFeatures = []
                         , insertModel = Nothing
            }, cmd )

        GotMachines (Ok machines) ->
            ( { model | machines = machines }, Cmd.none )
        
        GotMachines (Err _) ->
            ( model, Cmd.none )

        GotMachine (Ok machineInfo) ->
            ( { model | selectedMachine = Just machineInfo }, Cmd.none )

        GotMachine (Err _) ->
            ( { model | selectedMachine = Nothing }, Cmd.none )

        MachineInfoClosed ->
            ( { model | selectedMachine = Nothing }, Cmd.none )

        InsertModeEntered ->
            ( { model | selectedMachine = Nothing, insertModel = Just InsertMachine.init }, Cmd.none )

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
                                    ( { model | insertModel = Nothing }, Requests.postMachine machine (wrapMsg << GotMachine)
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

        GotAbout (Ok about) ->
            ( { model | about = about }, Cmd.none )

        GotAbout (Err _) ->
            ( model, Cmd.none )


timePeriodToString : (Time, Time) -> (String, String)
timePeriodToString (from, to) =
    let addZero n = if n < 10 then "0" ++ (String.fromInt n) else String.fromInt n
        fromString = addZero from.hours ++ ":" ++ addZero from.minutes
        toString = addZero to.hours ++ ":" ++ addZero to.minutes
    in
    (fromString, toString)

--| change state of hovered features on the map


hoveredFeatures : List Json.Encode.Value -> MapboxAttr msg
hoveredFeatures =
    List.map (\feat -> ( feat, [ ( "hover", Json.Encode.bool True ) ] ))
        >> featureState


view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
    let
        --| floating content can be either region info, event info or about text
        --| depending on the current mode
        content =
            case model.mode of
                Loading ->
                    div [] []

                Machines ->
                    viewMachineInfo (wrapMsg MachineInfoClosed) model.selectedMachine

                Products ->
                    div [] []

                About ->
                    Html.div Styles.Attributes.about
                        [ Html.p [] [ Html.text model.about ]
                        ]

        --| button to start inserting a new event
        insertButton =
            if model.mode == Machines && model.insertModel == Nothing then
                Html.button
                    (Styles.Attributes.insertButton
                        ++ [ Html.Events.onClick (wrapMsg InsertModeEntered) ]
                    )
                    [ text "Add Machine" ]
            else
                Html.div [] []

        insertForm =
            case model.insertModel of
                Just insertModel ->
                    InsertMachine.view (wrapMsg << InsertMachineMsg) insertModel

                Nothing ->
                    Html.div [] []

    in
    div []
        [viewMap wrapMsg model
        , content
        , insertButton
        , insertForm
        ]



--| View the map based on the current mode
--| Sources are loaded from geojson code chunks


viewMap : (Msg -> msg) -> Model -> Html msg
viewMap wrapMsg model =
    let
        --| layers of the map are based on the current mode
        modeLayers =
            case model.mode of

                Machines ->
                    layersFromMachines model.machines

                _ ->
                    []

        --| layers of the map that listen to events
        modeListenLayers =
            case model.mode of
                Machines ->
                    listenLayersFromMachines model.machines

                _ ->
                    []

        --| sources of the map are based on the current mode
        modeSources =
            case model.mode of
                
                Machines ->
                    sourcesFromMachines model.machines

                _ ->
                    []
    in
    div Styles.Attributes.map
        [ map
            [ maxZoom 18
            , minZoom 10
            , maxBounds
                --| limit the map to Prague and its surroundings
                ( LngLat 14.098789849977067 49.932573881803535
                , LngLat 14.750530939532837 50.2500770495798
                )
            , onMouseMove (wrapMsg << Hover)
            , onClick (wrapMsg << Click)
            , onMouseOut (wrapMsg << MovedOut)
            , id "paq-map"
            , eventFeaturesLayers modeListenLayers
            , hoveredFeatures model.hoveredFeatures --| highlight hovered features
            ]
            (Style
                { transition = Style.defaultTransition
                , light = Style.defaultLight
                , sources =
                    Source.vectorFromUrl "composite" "mapbox://mapbox.mapbox-terrain-v2,mapbox.mapbox-streets-v7"
                        :: modeSources
                , misc =
                    [ Style.defaultCenter <| LngLat 14.417941209392495 50.10093189854709
                    , Style.defaultZoomLevel 13
                    , Style.sprite "mapbox://sprites/mapbox/streets-v9"
                    , Style.glyphs "mapbox://fonts/mapbox/{fontstack}/{range}.pbf"
                    ]
                , layers =
                    --| layers that are always present
                    styleLayers
                        --| layers that depend on the current mode
                        ++ modeLayers
                }
            )
        ]
