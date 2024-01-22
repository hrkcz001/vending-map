module InsertMachine exposing (Model, Msg(..), init, update, view)

import Html exposing (Html)
import Html.Attributes
import Html.Events
import LngLat exposing (LngLat)
import TimePicker exposing (TimeEvent(..), TimePicker, Time)

import Styles.Attributes
import Html exposing (time)

type alias Model =
    { insertedPoint : Maybe LngLat
    , insertedAddress : Maybe String
    , insertedDetails : Maybe String
    , insertedTimeFrom : Maybe Time
    , insertedTimeTo : Maybe Time
    , timeFromPicker : TimePicker
    , timeToPicker : TimePicker
    }

type Msg
    = PointChoosed (Maybe LngLat)
    | AddressInserted String
    | DetailsInserted String
    | FromTimeMsg TimePicker.Msg
    | ToTimeMsg TimePicker.Msg
    | InsertCancelled
    | InsertSubmitted

init : Model
init =
    { insertedPoint = Nothing
    , insertedAddress = Nothing
    , insertedDetails = Nothing
    , insertedTimeFrom = Nothing
    , insertedTimeTo = Nothing
    , timeFromPicker = (TimePicker.init Nothing)
    , timeToPicker = (TimePicker.init Nothing)
    }

steppingSettings : TimePicker.Settings
steppingSettings =
    let
        defaultSettings =
            TimePicker.defaultSettings
    in
    { defaultSettings | showSeconds = False, minuteStep = 15, use24Hours = True }

update : Msg -> Model -> Model
update msg model =
    case msg of
        PointChoosed point ->
            { model | insertedPoint = point }

        AddressInserted address ->
            { model | insertedAddress = Just address }

        DetailsInserted details ->
            { model | insertedDetails = Just details }

        FromTimeMsg timeMsg ->
            let
                ( updatedPicker, timeEvent ) =
                    TimePicker.update steppingSettings timeMsg model.timeFromPicker
                timeFrom =
                    case timeEvent of
                        Changed time ->
                            time
                        NoChange ->
                            model.insertedTimeFrom
            in
            { model | timeFromPicker = updatedPicker, insertedTimeFrom = timeFrom }
        ToTimeMsg timeMsg ->
            let
                ( updatedPicker, timeEvent ) =
                    TimePicker.update steppingSettings timeMsg model.timeToPicker
                timeTo =
                    case timeEvent of
                        Changed time ->
                            time
                        NoChange ->
                            model.insertedTimeTo
            in
            { model | timeToPicker = updatedPicker, insertedTimeTo = timeTo }
        _ ->
            model

view : (Msg -> msg ) -> Model -> Html msg
view wrapMsg model =
    case model.insertedPoint of
        Nothing ->
            Html.div []
                [ Html.div Styles.Attributes.choosePointHint
                    [Html.text "Choose a point on the map"]
                , Html.button
                    (Styles.Attributes.insertButton
                        ++ [ Html.Events.onClick (wrapMsg InsertCancelled) ]
                    )
                    [ Html.text "Cancel" ]
                ]
        Just _ ->
            Html.div Styles.Attributes.machineInfo
                [ Html.input
                    (Styles.Attributes.inputAddress
                        ++ [ Html.Attributes.placeholder "Address"
                           , Html.Events.onInput (wrapMsg << AddressInserted)
                           ]
                    )
                    []
                , Html.textarea
                    (Styles.Attributes.inputDetails
                        ++ [ Html.Attributes.placeholder "Details"
                           , Html.Events.onInput (wrapMsg << DetailsInserted)
                           ]
                    )
                    []
                , Html.div Styles.Attributes.availableHours
                    [ Html.text "Available hours"
                    , Html.br [] [], Html.br [] []
                    , Html.text "From:"
                    , Html.br [] [], Html.br [] []
                    , Html.text "To:" ]
                , Html.div (Styles.Attributes.inputTimeFrom ++
                            [ Html.Attributes.class "default-time-picker" ])
                [ Html.map (wrapMsg << FromTimeMsg) <| TimePicker.view steppingSettings model.timeFromPicker ]
                , Html.div (Styles.Attributes.inputTimeTo ++
                            [ Html.Attributes.class "default-time-picker" ])
                [ Html.map (wrapMsg << ToTimeMsg) <| TimePicker.view steppingSettings model.timeToPicker ]
                , Html.button
                    (Styles.Attributes.closeButton
                        ++ [ Html.Events.onClick (wrapMsg InsertCancelled) ]
                    )
                    [ Html.text "X" ]

                --| disable submit button if name or details are empty
                , Html.button
                    (Styles.Attributes.insertingSubmit
                        ++ [ Html.Attributes.disabled <|
                                model.insertedAddress
                                    == Nothing
                                    || model.insertedAddress
                                    == Just ""
                                    || model.insertedDetails
                                    == Just ""
                                    || timePeriodNotValid model.insertedTimeFrom model.insertedTimeTo
                           , Html.Events.onClick (wrapMsg InsertSubmitted)
                           ]
                    )
                    [ Html.text "Submit" ]
                ]

timePeriodNotValid : Maybe Time -> Maybe Time -> Bool
timePeriodNotValid timeFrom timeTo =
    case (timeFrom, timeTo) of
        (Nothing, Nothing) ->
            False
        (Just from, Just to) ->
            if from.hours > to.hours then
                True
            else if from.hours == to.hours then
                from.minutes >= to.minutes
            else
                False
        _ -> True