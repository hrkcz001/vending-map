module Map exposing (Mode(..), Model, Msg(..), init, update, view)

import Machine exposing (layersFromMachines, listenLayersFromMachines, sourcesFromMachines)
import Products
import Html exposing (Html, div)
import Http
import Json.Decode
import Json.Encode
import LngLat exposing (LngLat)
import Mapbox.Element exposing (..)
import Mapbox.Source as Source
import Mapbox.Style as Style exposing (Style(..))
import Requests exposing (getAbout)
import Styles.Attributes
import Styles.Streets exposing (styleLayers)
import Machine exposing (selectedPointLayer)
import Machine exposing (selectedPointSource)

type alias Model =
    { hoveredFeatures : List Json.Encode.Value
    , machineModel : Machine.Model
    , productsModel : Products.Model
    , mode : Mode
    , about : String
    }


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
    | MachineMsg Machine.Msg
    | ProductsMsg Products.Msg
    | GotAbout (Result Http.Error String)


init : Model
init =
    { hoveredFeatures = []
    , mode = Loading
    , machineModel = Machine.init
    , productsModel = Products.init
    , about = "Loading..."
    }


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
                        Machines -> let
                                        ( newMachineModel, machineCmd ) = Machine.update (wrapMsg << MachineMsg) 
                                                                        (Machine.MapClicked (featType, featName, lngLat))
                                                                        model.machineModel
                                    in
                                    ({ model | machineModel = newMachineModel }, machineCmd)
                        _ ->
                            ( model, Cmd.none)
            in
            ( newModel, cmd )

        SetMode mode ->
            let
                ( newModel, cmd ) =
                    case mode of
                        Products ->
                                    let
                                        ( newProductsModel, productsCmd ) = 
                                            Products.update (wrapMsg << ProductsMsg) 
                                            Products.RequestedProducts
                                            model.productsModel
                                    in
                                    ({ model | productsModel = newProductsModel }, productsCmd)

                        Machines ->
                                    let
                                        ( newMachineModel, machineCmd ) = 
                                            Machine.update (wrapMsg << MachineMsg) 
                                            Machine.RequestedMachines
                                            model.machineModel
                                    in
                                    ({ model | machineModel = newMachineModel }, machineCmd)

                        About ->
                            ( { model | about = "Loading..."
                            }, getAbout (wrapMsg << GotAbout) )
                        
                        _ ->
                            ( model, Cmd.none )
            in
            ( { newModel | mode = mode
                         , hoveredFeatures = []
            }, cmd )

        MachineMsg machineMsg ->
            let
                ( newMachineModel, cmd ) =
                    Machine.update (wrapMsg << MachineMsg) machineMsg model.machineModel
            in
            ( { model | machineModel = newMachineModel }, cmd )
        
        ProductsMsg productsMsg ->
            let
                ( newProductsModel, cmd ) =
                    Products.update (wrapMsg << ProductsMsg) productsMsg model.productsModel
            in
            ( { model | productsModel = newProductsModel }, cmd )

        GotAbout (Ok about) ->
            ( { model | about = about }, Cmd.none )

        GotAbout (Err _) ->
            ( model, Cmd.none )


--| change state of hovered features on the map


hoveredFeatures : List Json.Encode.Value -> MapboxAttr msg
hoveredFeatures =
    List.map (\feat -> ( feat, [ ( "hover", Json.Encode.bool True ) ] ))
        >> featureState


view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
    let
        content =
            case model.mode of
                Loading ->
                    div [] []

                Machines -> Machine.view (wrapMsg << MachineMsg) model.machineModel

                Products -> Products.view (wrapMsg << ProductsMsg) model.productsModel

                About ->
                    Html.div Styles.Attributes.about
                        [ Html.p [] [ Html.text model.about ]
                        ]

    in
    div []
        [viewMap wrapMsg model
        , content
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
                    layersFromMachines model.machineModel ++
                    selectedPointLayer model.machineModel

                _ ->
                    []

        --| layers of the map that listen to events
        modeListenLayers =
            case model.mode of
                Machines ->
                    listenLayersFromMachines model.machineModel

                _ ->
                    []

        --| sources of the map are based on the current mode
        modeSources =
            case model.mode of
                
                Machines ->
                    sourcesFromMachines model.machineModel ++
                    selectedPointSource model.machineModel

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
