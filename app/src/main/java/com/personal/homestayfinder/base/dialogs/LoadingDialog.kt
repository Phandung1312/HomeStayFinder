package com.personal.homestayfinder.base.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.WindowManager
import com.personal.homestayfinder.R

class LoadingDialog constructor(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        val params : WindowManager.LayoutParams = window!!.attributes
        params.gravity = Gravity.CENTER
        window!!.attributes = params
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)

    }
}