module Styles.Attributes exposing
    ( about
    , active
    , closeButton
    , entry
    , floating
    , machineInfo
    , machineDetails
    , header
    , headerBackground
    , availableHours
    , productsList
    , cornerButton
    , reviewsList
    , reviewInputForm
    , reviewInputComment
    , reviewRating
    , reviewSubmit
    , inputAddress
    , inputDetails
    , inputTimeFrom
    , inputTimeTo
    , insertButton
    , priceInput
    , thumbnail
    , rangeSlider
    , refreshButton
    , choosePointHint
    , insertingSubmit
    , map
    , titleName
    )

import Html exposing (Attribute)
import Html.Attributes as Attributes


map : List (Attribute msg)
map =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "0"
    , Attributes.style "width" "100%"
    , Attributes.style "height" "93%"
    , Attributes.style "top" "7%"
    , Attributes.style "left" "0%"
    , Attributes.style "overflow" "hidden"
    ]


headerBackground : List (Attribute msg)
headerBackground =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "0"
    , Attributes.style "width" "100%"
    , Attributes.style "height" "7%"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "0%"
    , Attributes.style "background-color" "ghostwhite"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "border-bottom" "1px solid #000"
    ]


titleName : List (Attribute msg)
titleName =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "1"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "0%"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "color" "darkcyan"
    , Attributes.style "padding" "0.5rem"
    , Attributes.style "font-size" "2.125rem"
    ]


header : List (Attribute msg)
header =
    [ Attributes.style "position" "absolute"
    , Attributes.style "text-align" "center"
    , Attributes.style "display" "table"
    , Attributes.style "z-index" "1"
    , Attributes.style "width" "50%"
    , Attributes.style "height" "7%"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "25%"
    ]


entry : List (Attribute msg)
entry =
    [ Attributes.style "display" "table-cell"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "vertical-align" "middle"
    ]


active : List (Attribute msg)
active =
    [ Attributes.style "text-decoration" "underline"
    , Attributes.style "color" "darkgreen"
    ]


closeButton : List (Attribute msg)
closeButton =
    [ Attributes.style "position" "absolute"
    , Attributes.style "top" "0"
    , Attributes.style "right" "0"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "font-weight" "bold"
    , Attributes.style "color" "#000"
    , Attributes.style "text-decoration" "none"
    ]


insertButton : List (Attribute msg)
insertButton =
    [ Attributes.style "position" "absolute"
    , Attributes.style "top" "9%"
    , Attributes.style "right" "2%"
    , Attributes.style "font-size" "1.2rem"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "color" "#000"
    , Attributes.style "text-decoration" "none"
    ]

choosePointHint : List (Attribute msg)
choosePointHint =
    [ Attributes.style "position" "absolute"
    , Attributes.style "text-align" "center"
    , Attributes.style "top" "10%"
    , Attributes.style "left" "0%"
    , Attributes.style "width" "100%"
    , Attributes.style "font-size" "1.5rem"
    , Attributes.style "font-weight" "bold"
    , Attributes.style "color" "#000"
    , Attributes.style "text-decoration" "none"
    ]

rangeSlider : List (Attribute msg)
rangeSlider =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "2"
    , Attributes.style "top" "8%"
    , Attributes.style "left" "1%"
    ]

refreshButton : List (Attribute msg)
refreshButton =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "2"
    , Attributes.style "top" "8%"
    , Attributes.style "left" "5%"
    , Attributes.style "cursor" "pointer"
    ]


floating : List (Attribute msg)
floating =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "2"
    , Attributes.style "width" "60%"
    , Attributes.style "height" "60%"
    , Attributes.style "top" "20%"
    , Attributes.style "left" "20%"
    , Attributes.style "overflow" "auto"
    , Attributes.style "background-color" "FloralWhite"
    , Attributes.style "padding" "1rem"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "border" "1px solid #000"
    ]


machineInfo : List (Attribute msg)
machineInfo =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "50%"
    , Attributes.style "height" "100%"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "0%"
    , Attributes.style "padding" "1rem"
    , Attributes.style "overflow" "auto"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "border" "1px solid #000"
    ]


productsList : List (Attribute msg)
productsList =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "50%"
    , Attributes.style "height" "100%"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "50%"
    , Attributes.style "padding" "1rem"
    , Attributes.style "overflow" "auto"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "border" "1px solid #000"
    ]


priceInput : List (Attribute msg)
priceInput =
    [ Attributes.style "width" "20%"
    , Attributes.style "height" "5%"
    , Attributes.style "background-color" "#fff"
    , Attributes.style "border" "1px solid #000"
    ]


machineDetails : List (Attribute msg)
machineDetails =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "50%"
    , Attributes.style "height" "100%"
    ]


cornerButton : List (Attribute msg)
cornerButton =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "20%"
    , Attributes.style "height" "5%"
    , Attributes.style "top" "90%"
    , Attributes.style "left" "5%"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "color" "#000"
    , Attributes.style "text-decoration" "none"
    ]


reviewsList : List (Attribute msg)
reviewsList =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "70%"
    , Attributes.style "height" "100%"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "0%"
    , Attributes.style "overflow" "auto"
    , Attributes.style "background-color" "FloralWhite"
    , Attributes.style "padding" "1rem"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "border" "1px solid #000"
    ]

reviewInputForm : List (Attribute msg)
reviewInputForm =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "30%"
    , Attributes.style "height" "100%"
    , Attributes.style "top" "0%"
    , Attributes.style "left" "70%"
    ]

reviewInputComment : List (Attribute msg)
reviewInputComment =
    [ Attributes.style "position" "absolute"
    , Attributes.style "resize" "none"
    , Attributes.style "width" "90%"
    , Attributes.style "height" "45%"
    , Attributes.style "top" "10%"
    , Attributes.style "left" "5%"
    , Attributes.style "background-color" "#fff"
    , Attributes.style "border" "1px solid #000"
    ]


reviewRating : List (Attribute msg)
reviewRating =
    [ Attributes.style "position" "absolute"
    , Attributes.style "top" "70%"
    , Attributes.style "left" "15%"
    ]


reviewSubmit : List (Attribute msg)
reviewSubmit =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "20%"
    , Attributes.style "height" "5%"
    , Attributes.style "top" "90%"
    , Attributes.style "left" "40%"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "color" "#000"
    , Attributes.style "text-decoration" "none"
    ]


inputAddress : List (Attribute msg)
inputAddress =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "30%"
    , Attributes.style "height" "5%"
    , Attributes.style "top" "5%"
    , Attributes.style "left" "10%"
    , Attributes.style "background-color" "#fff"
    , Attributes.style "border" "1px solid #000"
    ]


inputDetails : List (Attribute msg)
inputDetails =
    [ Attributes.style "position" "absolute"
    , Attributes.style "resize" "none"
    , Attributes.style "width" "80%"
    , Attributes.style "height" "40%"
    , Attributes.style "top" "15%"
    , Attributes.style "left" "10%"
    , Attributes.style "background-color" "#fff"
    , Attributes.style "border" "1px solid #000"
    ]

availableHours : List (Attribute msg)
availableHours =
    [ Attributes.style "position" "absolute"
    , Attributes.style "top" "60%"
    , Attributes.style "left" "10%"
    ]


inputTimeFrom : List (Attribute msg)
inputTimeFrom =
    [ Attributes.style "position" "absolute"
    , Attributes.style "top" "67%"
    , Attributes.style "left" "17%"
    ]

inputTimeTo : List (Attribute msg)
inputTimeTo =
    [ Attributes.style "position" "absolute"
    , Attributes.style "top" "77%"
    , Attributes.style "left" "17%"
    ]


insertingSubmit : List (Attribute msg)
insertingSubmit =
    [ Attributes.style "position" "absolute"
    , Attributes.style "width" "10%"
    , Attributes.style "height" "5%"
    , Attributes.style "top" "90%"
    , Attributes.style "left" "45%"
    , Attributes.style "cursor" "pointer"
    , Attributes.style "color" "#000"
    , Attributes.style "text-decoration" "none"
    ]


thumbnail : List (Attribute msg)
thumbnail =
    [ Attributes.style "width" "30%"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "overflow" "hidden"
    , Attributes.style "border" "1px solid #000"
    ]


about : List (Attribute msg)
about =
    [ Attributes.style "position" "absolute"
    , Attributes.style "z-index" "2"
    , Attributes.style "width" "70%"
    , Attributes.style "height" "60%"
    , Attributes.style "top" "20%"
    , Attributes.style "left" "15%"
    , Attributes.style "overflow" "auto"
    , Attributes.style "white-space" "pre-wrap"
    , Attributes.style "background-color" "FloralWhite"
    , Attributes.style "padding" "1rem"
    , Attributes.style "box-sizing" "border-box"
    , Attributes.style "border" "1px solid #000"
    ]
