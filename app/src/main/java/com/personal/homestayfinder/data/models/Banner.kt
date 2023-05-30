package com.personal.homestayfinder.data.models

import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class Banner(
    var imageUrl : String? = null
){
    fun toSlideModel() : SlideModel{
        return SlideModel(
            imageUrl,
            ScaleTypes.CENTER_CROP
        )
    }
}
