package com.personal.homestayfinder.data.models

data class SearchFilter(
    var minPrice : Long = 500000,
    var maxPrice : Long = Long.MAX_VALUE,
    var roomType : RoomType? = null,
    var utilitiesList : MutableList<Utility> = ArrayList(),
    var capacity : Int? = 0,
    var gender : Int? = 1
)