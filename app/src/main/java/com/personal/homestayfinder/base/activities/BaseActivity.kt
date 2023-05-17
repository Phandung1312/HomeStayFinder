package com.personal.homestayfinder.base.activities

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.personal.homestayfinder.base.dialogs.ConfirmDialog
import com.personal.homestayfinder.base.dialogs.ErrorDialog
import com.personal.homestayfinder.base.dialogs.LoadingDialog
import com.personal.homestayfinder.base.dialogs.NotifyDialog

open class BaseActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(this)
    }

    open fun showScreenLoading(isShow: Boolean){
        if(isShow){
            loadingDialog.show()
        }
        else{
            loadingDialog.dismiss()
        }
    }

    open fun showErrorDialog(message: String) {
        val errorDialog = ErrorDialog(this, message)
        errorDialog.show()
        errorDialog.window?.setGravity(Gravity.CENTER)
        errorDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showNotifyDialog(
        titleResourceId: Int,
        messageResourceId: Int,
        textButtonResourceId: Int = -1
    ) {
        val title = getString(titleResourceId)
        val message = getString(messageResourceId)
        val textButton = if (textButtonResourceId == -1) null else getString(textButtonResourceId)
        showNotifyDialog(message, title, textButton)
    }

    open fun showNotifyDialog(message: String, title: String, textButton: String? = null) {
        val notifyDialog = NotifyDialog(this, title, message, textButton)
        notifyDialog.show()
        notifyDialog.window?.setGravity(Gravity.CENTER)
        notifyDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showConfirmDialog(
        titleResourceId: Int,
        messageResourceId: Int = -1,
        positiveTitleResourceId: Int,
        negativeTitleResourceId: Int,
        callback: ConfirmDialog.ConfirmCallback
    ) {
        val title = getString(titleResourceId)
        val message = if (messageResourceId != -1) getString(messageResourceId) else null
        val negativeButtonTitle = getString(negativeTitleResourceId)
        val positiveButtonTitle = getString(positiveTitleResourceId)

        showConfirmDialog(
            title,
            message,
            positiveButtonTitle,
            negativeButtonTitle,
            callback
        )
    }

    open fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String,
        negativeButtonTitle: String,
        callback: ConfirmDialog.ConfirmCallback
    ) {
        val confirmDialog = ConfirmDialog(
            context = this,
            title = title,
            message = message,
            positiveButtonTitle = positiveButtonTitle,
            negativeButtonTitle = negativeButtonTitle,
            callback = callback
        )
        if(!isFinishing){
            confirmDialog.show()
            confirmDialog.window?.setGravity(Gravity.CENTER)
            confirmDialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}