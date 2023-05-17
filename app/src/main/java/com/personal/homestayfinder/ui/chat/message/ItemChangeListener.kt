package com.personal.homestayfinder.ui.chat.message

import com.personal.homestayfinder.data.models.Message

interface ItemChangeListener {
    fun seen(message : Message)
    fun onScrollRecyclerView(position : Int)
}