module MachineInfo exposing (Model, Msg(..), init, update, view)

import Types exposing (Machine, Review, Product, ProductInMachine)
import Html exposing (Html)
import Styles.Attributes
import Html.Events
import Http
import Requests exposing (getReviews, postReview, getProducts, getProductsOfMachine, addProductToMachine, updateProductInMachine)
import Html.Attributes
import Rating

type alias Model =
    { selectedMachine : Machine
    , reviews : Maybe (List Review)
    , reviewComment : Maybe String
    , ratingState : Rating.State
    , products : Maybe (List ProductInMachine)
    , candidateProducts : Maybe (List Product)
    , candidatePrice : Maybe String
    }

type Msg = MachineInfoClosed
         | ReviewsRequested
         | CandidatesRequested
         | GotReviews (Result Http.Error (List Review))
         | GotReview (Result Http.Error Review)
         | GotProducts (Result Http.Error (List ProductInMachine))
         | GotProduct (Result Http.Error ProductInMachine)
         | UpdatedProduct (Result Http.Error ProductInMachine)
         | GotCandidates (Result Http.Error (List Product))
         | ReviewsClosed
         | ReviewCommentInserted String
         | RatingMsg Rating.Msg
         | ReviewSubmitted
         | CandidatesClosed
         | PriceInserted String
         | CandidateSelected Product
         | ChangeProductStatus ProductInMachine

init : (Msg -> msg) -> Machine -> (Model, Cmd msg)
init wrapMsg machine =
    ({ selectedMachine = machine 
    , reviews = Nothing
    , reviewComment = Nothing
    , ratingState = Rating.initialState |> Rating.set 3
    , products = Nothing
    , candidateProducts = Nothing
    , candidatePrice = Nothing
    }, getProductsOfMachine machine.id (wrapMsg << GotProducts))

update : (Msg -> msg) -> Msg -> Model -> ( Model, Cmd msg )
update wrapMsg msg model =
    case msg of

        MachineInfoClosed ->
            ( model, Cmd.none )
        
        ReviewsRequested -> 
            ( model, getReviews model.selectedMachine.id (wrapMsg << GotReviews) )

        CandidatesRequested -> 
            ( model, getProducts (Just model.selectedMachine.id) (wrapMsg << GotCandidates) )

        ReviewsClosed ->
            ( { model | reviews = Nothing, reviewComment = Nothing }, Cmd.none )
        
        CandidatesClosed ->
            ( { model | candidateProducts = Nothing, candidatePrice = Nothing }, Cmd.none )
        
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

        GotProducts (Ok products) ->
            ( { model | products = Just products }, Cmd.none )

        GotProducts (Err _) ->
            ( model, Cmd.none )
        
        GotProduct (Ok product) ->
            ( { model | products = Maybe.map (\products -> products ++ [product]) model.products }, Cmd.none )
        
        GotProduct (Err _) ->
            ( model, Cmd.none )
        
        UpdatedProduct (Ok product) ->
            case model.products of
                Nothing -> (model, Cmd.none)
                Just productList ->
                    let
                        res = findAndUpdate product productList
                        (newModel, cmd) = case res of
                                            Nothing -> (model, Cmd.none)
                                            Just newProductsList -> ( { model | products = Just newProductsList }, Cmd.none )
                    in
                    ( newModel, cmd )
        
        UpdatedProduct (Err _) ->
            ( model, Cmd.none )

        GotCandidates (Ok products) ->
            ( { model | candidateProducts = Just products }, Cmd.none )

        GotCandidates (Err _) ->
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
        
        PriceInserted price ->
            ( { model | candidatePrice = Just price }, Cmd.none )
        
        CandidateSelected product ->
            let
                machineId = model.selectedMachine.id
                price = Maybe.withDefault 0 <| String.toFloat <| Maybe.withDefault "0" model.candidatePrice
                productInMachine = { product = product, price = price, isAvailable = True }
            in
            ( { model | candidateProducts = Nothing, candidatePrice = Nothing }, addProductToMachine machineId productInMachine (wrapMsg << GotProduct) )

        ChangeProductStatus productInMachine ->
            let
                newProduct = { productInMachine | isAvailable = not productInMachine.isAvailable }
            in
            ( model, updateProductInMachine model.selectedMachine.id newProduct (wrapMsg << UpdatedProduct) )

findAndUpdate : ProductInMachine -> List ProductInMachine -> Maybe (List ProductInMachine)
findAndUpdate product products =
    let
        (before, after) = split (\p -> p.product.id == product.product.id) products
        findedProduct = List.head after
        newProduct = Maybe.map (\p -> { p | isAvailable = product.isAvailable, price = product.price }) findedProduct
    in
    Maybe.map2 (\p a -> before ++ p :: a) newProduct <| List.tail after

split : (a -> Bool) -> List a -> (List a, List a)
split predicate list =
    case list of
        [] -> ([], [])
        x :: xs -> if predicate x
                        then ([], list)
                        else let (before, after) = split predicate xs in (x :: before, after)

view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
    let
        productsPart = case model.candidateProducts of
                            Nothing -> viewProducts wrapMsg model.products
                            Just candidates -> viewCandidates wrapMsg model candidates
        (content, closeMsg) =
            case model.reviews of
                Nothing ->
                        ([ Html.div Styles.Attributes.machineInfo
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
                                    (Styles.Attributes.cornerButton
                                    ++ [ Html.Events.onClick (wrapMsg ReviewsRequested) ]
                                    )
                                    [ Html.text "Show reviews" ]
                                ]
                        , productsPart
                        ], wrapMsg MachineInfoClosed)

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
    
viewProducts : (Msg -> msg) -> Maybe (List ProductInMachine) -> Html msg
viewProducts wrapMsg maybeProducts =
    let
        price product = if product.isAvailable 
                        then String.fromFloat product.price ++ " Kč" 
                        else "Sold out :("
        
        changeButtonText product = if product.isAvailable 
                                    then "Sold out"
                                    else "Available again"

        productsHtml = case maybeProducts of 
                            Just products -> if List.isEmpty products
                                                then [ Html.text "No products available" ]
                                                else List.map (\product -> Html.div []
                                                            [ Html.text (product.product.name)
                                                            , Html.span [ Html.Attributes.style "float" "right"] 
                                                                        [ Html.button 
                                                                            [ Html.Events.onClick (wrapMsg <| ChangeProductStatus product)] 
                                                                            [ Html.text <| changeButtonText product ]
                                                                        ]
                                                            , Html.br [] []
                                                            , Html.span [ Html.Attributes.style "color" (pricesDiffToHtmlColor product.price product.product.averagePrice) ] 
                                                                        [ Html.text <| "-- " ++ (price product) ]
                                                            , Html.br [] []
                                                            , Html.br [] []
                                                            ]) products
                            Nothing -> [ Html.text "Loading products..." ]
    in
    Html.div Styles.Attributes.productsList <|
        Html.h2 [] [ Html.text "Products" ] ::
        productsHtml ++
        [ Html.button (Styles.Attributes.cornerButton ++ [Html.Events.onClick (wrapMsg CandidatesRequested)]) [ Html.text "Add Product" ] ]


viewCandidates : (Msg -> msg) -> Model -> List Product -> Html msg
viewCandidates wrapMsg model candidates =
    let
        priceInput = [ Html.input (Styles.Attributes.priceInput ++ 
                                [ Html.Attributes.placeholder "Price"
                                , Html.Attributes.value (Maybe.withDefault "" model.candidatePrice)
                                , Html.Events.onInput (wrapMsg << PriceInserted)
                                ]) []
                     , Html.br [] []
                     , Html.br [] []
                     ]
        productsHtml = if List.isEmpty candidates
                            then [ Html.text "No further products available" ]
                            else List.map (\product ->
                                Html.div []
                                [ Html.button [ Html.Attributes.disabled <| priceNotValid model.candidatePrice
                                              , Html.Events.onClick ( wrapMsg <| CandidateSelected product)] [ Html.text "Select" ]
                                , Html.text (" - " ++ product.name)
                                ]) candidates
    
    in
    Html.div Styles.Attributes.productsList <|
        priceInput ++
        productsHtml ++
        [ Html.button (Styles.Attributes.cornerButton ++ [Html.Events.onClick (wrapMsg CandidatesClosed)]) [ Html.text "Close" ] ]


priceNotValid : Maybe String -> Bool
priceNotValid maybePrice =
    case maybePrice of
        Nothing -> True
        Just price -> String.toFloat price == Nothing

pricesDiffToHtmlColor : Float -> Maybe Float -> String
pricesDiffToHtmlColor price1 maybePrice2 =
    case maybePrice2 of
        Nothing -> "black"
        Just price2 ->
            if price1 > price2
                then "red"
                else if price1 < price2
                    then "green"
                    else "black"