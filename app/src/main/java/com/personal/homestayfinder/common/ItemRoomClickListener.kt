package com.personal.homestayfinder.common

import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.RoomListItem

interface ItemRoomClickListener {
    fun onRoomClick(room : RoomListItem)
}