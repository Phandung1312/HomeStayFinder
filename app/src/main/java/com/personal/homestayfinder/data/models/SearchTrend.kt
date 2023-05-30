package com.personal.homestayfinder.data.models

data class SearchTrend(
    var cityId : Int? = 0,
    var districtId : Int? = 0,
    var districtName : String? = null,
    var imageUrl : String? = null,
    var numOfSearches : Int? = 0
)