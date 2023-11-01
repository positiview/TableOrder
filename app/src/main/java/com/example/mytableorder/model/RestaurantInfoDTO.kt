package com.example.mytableorder.model

data class RestaurantInfoDTO(
    var name : String,
    //위도
    var latitude : Double,
    //경도
    var longitude : Double
)