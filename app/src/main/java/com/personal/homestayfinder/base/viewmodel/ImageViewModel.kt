package com.personal.homestayfinder.base.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData

open class ImageViewModel : BaseViewModel() {
    var imagesList = MutableLiveData<MutableList<Uri>>()
    open fun updateImagesValidate(){

    }
}