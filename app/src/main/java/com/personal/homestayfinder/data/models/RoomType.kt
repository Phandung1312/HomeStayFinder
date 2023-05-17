package com.personal.homestayfinder.data.models

 class RoomType(
        var id : Int? = null,
        var typeName : String? = null
)
 {
         companion object{
                 const val DORMITORY = 1
                 const val ROOM_FOR_RENT = 2
                 const val SHARED_ROOM = 3
                 const val WHOLE_HOUSE = 4
                 const val APARTMENT = 5
         }
 }
