package com.personal.homestayfinder.common

import android.view.View

interface ItemRVClickListener {
    fun onClick(view : View,position : Int, item : Any)
}