module Products exposing (Model, Msg(..), init, update, view)

import Types exposing (Product)
import Requests exposing (getProducts, postProduct)
import Http
import Html exposing (Html)
import Html.Events
import Html.Attributes
import Styles.Attributes

type alias Model = { products : List Product 
                   , insertedName : Maybe String
                   , insertedPicture : Maybe String
                   }

type Msg = RequestedProducts
         | GotProducts (Result Http.Error (List Product))
         | GotProduct (Result Http.Error Product)
         | InsertedName String
         | InsertedPicture String
         | InsertSubmitted

init : Model
init =  { products = []
        , insertedName = Nothing
        , insertedPicture = Nothing
        }

update : (Msg -> msg) -> Msg -> Model -> (Model, Cmd msg)
update wrapMsg msg model =
    case msg of

        RequestedProducts ->
            ( model, getProducts Nothing (wrapMsg << GotProducts) )

        GotProducts (Ok products) ->
            ( { model | products = products }, Cmd.none )

        GotProducts (Err _) ->
            ( model, Cmd.none )

        GotProduct (Ok product) ->
            ( { model | products = model.products ++ [product]}, Cmd.none )
        
        GotProduct (Err _) ->
            ( model, Cmd.none )

        InsertedName name ->
            ( { model | insertedName = Just name }, Cmd.none )

        InsertedPicture picture ->
            ( { model | insertedPicture = Just picture }, Cmd.none )

        InsertSubmitted ->
            let
                productToPost = case model.insertedName of
                    Just name ->
                        Just <| Product -1 name model.insertedPicture Nothing
                    _ ->
                        Nothing
                
                cmd = case productToPost of
                    Just product ->
                        postProduct product (wrapMsg << GotProduct)
                    _ ->
                        Cmd.none
            in
            ( { model | insertedName = Nothing, insertedPicture = Nothing }, cmd )


view : (Msg -> msg) -> Model -> Html msg
view wrapMsg model =
    Html.div Styles.Attributes.floating
        [ Html.h3 [] [ Html.text "Add new product" ]
        , Html.input [ Html.Attributes.placeholder "Name, 50 characters max"
                     , Html.Events.onInput (wrapMsg << InsertedName)
                     , Html.Attributes.style "width" "40%"
                     ] []
        , Html.br [] []
        , Html.input [ Html.Attributes.placeholder "Picture URL, 255 characters max"
                     , Html.Events.onInput (wrapMsg << InsertedPicture)
                     , Html.Attributes.style "width" "40%"
                     ] []
        , Html.br [] []
        , Html.button [ Html.Attributes.disabled <| (Maybe.withDefault True (Maybe.map (\name -> String.length name == 0 ||  String.length name > 50) model.insertedName))
                                                    || Maybe.withDefault False (Maybe.map (\url -> String.length url > 255) model.insertedPicture)
                      , Html.Events.onClick (wrapMsg InsertSubmitted) ] [ Html.text "Submit" ]
        , Html.h3 [] [ Html.text "Products" ]
        , Html.div [ Html.Attributes.style "display" "grid"
                   , Html.Attributes.style "grid-template-columns" "repeat(3, 1fr)"
                   , Html.Attributes.style "gap" "10px"] (List.map viewProduct model.products)
        ]

viewProduct : Product -> Html msg
viewProduct product =
    let
        nophoto = "https://st4.depositphotos.com/14953852/24787/v/450/depositphotos_247872612-stock-illustration-no-image-available-icon-vector.jpg"
    in
    Html.div [] [ Html.div Styles.Attributes.thumbnail [
                  Html.img [ Html.Attributes.src <| Maybe.withDefault nophoto product.picture
                           , Html.Attributes.style "width" "100%" ] []]
                , Html.br [] []
                , Html.text ("-- " ++ product.name)
                , Html.text <| Maybe.withDefault "" (Maybe.map (\price -> ", average price: " ++ String.fromFloat price) product.averagePrice)
                , Html.br [] []
                , Html.br [] []
                ]