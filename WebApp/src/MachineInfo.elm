module MachineInfo exposing (Model, Msg(..), init, update, view)

import Types exposing (Machine, Review)
import Html exposing (Html)
import Styles.Attributes
import Html.Events
import Http
import Requests exposing (getReviews)
import Html.Attributes
import Requests exposing (postReview)
import Rating

type alias Model =
    { selectedMachine : Machine
    , reviews : Maybe (List Review)
    , reviewComment : Maybe String
    , ratingState : Rating.State
    }

type Msg = MachineInfoClosed
         | ReviewsRequested
         | GotReviews (Result Http.Error (List Review))
         | GotReview (Result Http.Error Review)
         | ReviewsClosed
         | ReviewCommentInserted String
         | RatingMsg Rating.Msg
         | ReviewSubmitted

init : Machine -> Model
init machine =
    { selectedMachine = machine 
    , reviews = Nothing
    , reviewComment = Nothing
    , ratingState = Rating.initialState |> Rating.set 3
    }

update : (Msg -> msg) -> Msg -> Model -> ( Model, Cmd msg )
update wrapMsg msg model =
    case msg of
        MachineInfoClosed ->
            ( model, Cmd.none )
        
        ReviewsRequested -> 
            ( model, getReviews model.selectedMachine.id (wrapMsg << GotReviews) )

        ReviewsClosed ->
            ( { model | reviews = Nothing, reviewComment = Nothing }, Cmd.none )
        
        GotReviews (Ok reviews) ->
            ( { model | reviews = Just reviews, reviewComment = Nothing }, Cmd.none )

        GotReviews (Err _) ->
            ( model, Cmd.none )

        GotReview (Ok review) ->
            let
                oldSelectedMachine = model.selectedMachine
                newReviewsCount = oldSelectedMachine.reviewsCount + 1
                newRating = case oldSelectedMachine.rating of
                                Nothing -> Just (Basics.toFloat review.rating)
                                Just rating -> Just ((rating * (Basics.toFloat oldSelectedMachine.reviewsCount) + (Basics.toFloat review.rating)) / (Basics.toFloat newReviewsCount))
                newSelectedMachine = { oldSelectedMachine | reviewsCount = newReviewsCount, rating = newRating }
                    
            in
            
            ( { model | reviews = Maybe.map (\reviews -> reviews ++ [review]) model.reviews, selectedMachine = newSelectedMachine }, Cmd.none )
        
        GotReview (Err _) ->
            ( model, Cmd.none )
        
        ReviewCommentInserted comment ->
            ( { model | reviewComment = Just comment }, Cmd.none )
        
        RatingMsg message ->
            ({ model | ratingState = Rating.update message model.ratingState }, Cmd.none)
        
        ReviewSubmitted ->
            case model.reviewComment of
                Nothing ->
                    ( model, Cmd.none )
                
                Just comment ->
                    let
                        review = { comment = Just comment, rating = Rating.get model.ratingState }
                    in
                    ( { model | reviewComment = Nothing }, postReview model.selectedMachine.id review (wrapMsg << GotReview) )
        

view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
    let
        (content, closeMsg) =
            case model.reviews of
                Nothing ->
                        ([Html.div Styles.Attributes.machineInfo
                        [ Html.h2 [] [ Html.text model.selectedMachine.address ]
                        , Html.div [] (Maybe.withDefault [Html.text "No reviews yet"]
                                        <| Maybe.map (\rating -> [ Html.text (stars <| Basics.round rating)
                                                                 , Html.br [] []
                                                                 , Html.text (String.fromInt model.selectedMachine.reviewsCount ++ " reviews")]
                                                     ) model.selectedMachine.rating)
                        , Maybe.withDefault (Html.div [] []) 
                                <| Maybe.map (\description -> Html.p [] [ Html.text description ]) model.selectedMachine.description
                        , Maybe.withDefault (Html.div [] []) 
                                <| Maybe.map viewTimePeriod model.selectedMachine.availableTime
                        , Html.button 
                            (Styles.Attributes.reviewsShowButton
                                ++ [ Html.Events.onClick (wrapMsg ReviewsRequested) ]
                            )
                            [ Html.text "Show reviews" ]
                        
                        ]], wrapMsg MachineInfoClosed)

                Just reviews ->
                    ( [ Html.div Styles.Attributes.reviewsList
                        <| List.map (\review -> Maybe.withDefault (Html.div [] []) 
                                                <| Maybe.map (\comment -> Html.p [] [ Html.div [] [Html.text ("-- " ++ comment)]
                                                                                    , Html.br [] []
                                                                                    , Html.text (stars review.rating)
                                                                                    , Html.br [] []
                                                                                    ]) review.comment) reviews
                      , Html.div Styles.Attributes.reviewInputForm 
                        [ Html.textarea (Styles.Attributes.reviewInputComment ++ 
                                        [ Html.Attributes.placeholder "Comment, 300 characters max"
                                        , Html.Attributes.value (Maybe.withDefault "" model.reviewComment)
                                        , Html.Events.onInput (wrapMsg << ReviewCommentInserted)
                                        ]) []
                        , Html.div Styles.Attributes.reviewRating [Html.map (wrapMsg << RatingMsg) (Rating.styleView "red rating" [ ( "color", "gold" ), ( "font-size", "48px" ) ] model.ratingState)]
                        , Html.button (Styles.Attributes.reviewSubmit ++ 
                                      [ Html.Attributes.disabled <| Maybe.withDefault True (Maybe.map (\comment -> String.length comment > 255 || comment == "") model.reviewComment)
                                      , Html.Events.onClick (wrapMsg ReviewSubmitted) ]) [ Html.text "Submit" ]
                        ]
                      ]
                    , wrapMsg ReviewsClosed)
    in
    
    Html.div Styles.Attributes.floating
                (content ++           
                 [  Html.button
                    (Styles.Attributes.closeButton
                        ++ [ Html.Events.onClick closeMsg ]
                    )
                    [ Html.text "X" ]]
                )

stars : Int -> String
stars rating =
    String.repeat rating "★" ++ String.repeat (5 - rating) "☆"

viewTimePeriod : (String, String) -> Html msg
viewTimePeriod (from, to) =
    Html.div [] [ Html.br [] []
                , Html.text ("Available from " ++ from ++ " to " ++ to)
                ]