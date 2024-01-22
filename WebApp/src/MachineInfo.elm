module MachineInfo exposing (Model, Msg(..), init, update, view)

import Types exposing (Machine)
import Html exposing (Html)
import Styles.Attributes
import Html.Events

type alias Model =
    { selectedMachine : Machine
    }

type Msg = MachineInfoClosed

init : Machine -> Model
init machine =
    { selectedMachine = machine 
    }

update : (Msg -> msg) -> Msg -> Model -> ( Model, Cmd msg )
update wrapMsg msg model =
    case msg of
        MachineInfoClosed ->
            ( model, Cmd.none )

view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
    Html.div Styles.Attributes.machineInfo
                [ Html.h2 [] [ Html.text model.selectedMachine.address ]
                , Maybe.withDefault (Html.div [] []) 
                    <| Maybe.map (\description -> Html.p [] [ Html.text description ]) model.selectedMachine.description
                
                , Html.button
                    (Styles.Attributes.closeButton
                        ++ [ Html.Events.onClick (wrapMsg MachineInfoClosed) ]
                    )
                    [ Html.text "X" ]
                ]