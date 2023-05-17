package com.personal.homestayfinder.data.models

data class Message(
    var sender : String? = null,
    var receiver : String? = null,
    var content : String? = null,
    var imagesUrl : MutableList<String>? = null,
    var time : String? = null,
    var seen : Boolean? = false,
    var roomScheduled :RoomListItem? = null
)
