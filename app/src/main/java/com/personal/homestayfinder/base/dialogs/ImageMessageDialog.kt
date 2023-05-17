package com.personal.homestayfinder.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.personal.homestayfinder.R

class ImageMessageDialog(
    private val context : Context,
    private val imageUrl : String
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_message)
        val imageView = findViewById<ImageView>(R.id.iv_image_message)
        Glide.with(context)
            .load(imageUrl)
            .fitCenter()
            .into(imageView)
        val btnClose = findViewById<AppCompatButton>(R.id.btn_close)
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        window?.setBackgroundDrawableResource(R.color.transparent)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}