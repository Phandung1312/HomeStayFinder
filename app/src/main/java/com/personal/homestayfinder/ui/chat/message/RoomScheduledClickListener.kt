package com.personal.homestayfinder.ui.chat.message

import com.personal.homestayfinder.data.models.RoomListItem

interface RoomScheduledClickListener {
    fun onClick(roomScheduled : RoomListItem)
}