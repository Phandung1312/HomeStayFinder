package com.personal.homestayfinder.common

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.personal.homestayfinder.R
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.adapters.LocationAdapter
import com.personal.homestayfinder.adapters.MessageImageAdapter
import com.personal.homestayfinder.ui.chat.message.MessageImageClickListener
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imgMessageUrl")
    fun setImageMessage(iv : ImageView, imgMessageUrl : String?){
        imgMessageUrl?.let {
            iv.visibility = View.VISIBLE
            Glide.with(iv.context)
                .load(it)
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .into(iv)
        } ?: run {
            iv.visibility = View.GONE
        }
    }
    @JvmStatic
    @BindingAdapter("imgUrl")
    fun setImage(iv : ImageView, imgUrl : String?){
        imgUrl?.let {
            Glide.with(iv.context)
                .load(it)
                .placeholder(R.drawable.loading_animation)
                .apply(RequestOptions().centerCrop())
                .into(iv)
        }
    }
    @JvmStatic
    @BindingAdapter("imgUrlFullSize")
    fun setFullSizeImage(iv : ImageView, imgUrlFullSize : String?){
        imgUrlFullSize?.let {
            Glide.with(iv.context)
                .load(it)
                .fitCenter()
                .into(iv)
        }
    }
    @JvmStatic
    @BindingAdapter("locations")
    fun setDropDownListItems(view : AutoCompleteTextView,locations : List<Location>?){
        locations?.let {
            val adapter = LocationAdapter(view.context,locations)
            view.setAdapter(adapter)
        }
    }
    @JvmStatic
    @BindingAdapter("currentLocation")
    fun setItemView(view : AutoCompleteTextView,currentLocation : Location?){
        if (currentLocation == null){
            view.text = null
        }
    }
    @JvmStatic
    @BindingAdapter("onItemSelected")
    fun setOnItemSelected(view : AutoCompleteTextView,onItemSelected : (Location) -> Unit){
        view.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position)
            onItemSelected.invoke(item as Location)
        }
    }
    @JvmStatic
    @BindingAdapter("isValidate")
    fun setVisibilityErrorText(textView: TextView,isValidate : Boolean?){
        isValidate?.let {
            if(!isValidate){
                textView.visibility = View.VISIBLE
                return
            }
        }
        textView.visibility = View.GONE
    }
    @JvmStatic
    @BindingAdapter("gender")
    fun setGenderDrawable(textView: TextView, gender : Int?){
        val drawable = when (gender) {
            Constant.ALL_GENDER -> ContextCompat.getDrawable(textView.context, R.drawable.male_and_female)
            Constant.MALE -> ContextCompat.getDrawable(textView.context, R.drawable.male)
            Constant.FEMALE -> ContextCompat.getDrawable(textView.context, R.drawable.female)
            else -> null
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
    @JvmStatic
    @BindingAdapter("rentalPrice")
    fun setRentalPriceText(textView : TextView, rentalPrice : Long?){
        rentalPrice?.let {
            val formatterPrice = when {
                it >= 1000000000 -> "${it / 1000000000} tỉ"
                it >= 1000000 -> "${it / 1000000} triệu"
                else -> "${it / 1000}k"
            }
            textView.text = "$formatterPrice VND/phòng"
        }
    }
    @JvmStatic
    @BindingAdapter("price")
    fun setPriceText(textView : TextView, price : Long?){
        price?.let {
            val formatterPrice = when {
                it >= 1000000000 -> "${it / 1000000000} tỉ"
                it >= 1000000 -> "${it / 1000000} triệu"
                else -> "${it / 1000}k"
            }
            textView.text = formatterPrice
            return
        }
        textView.text = "Không có"
    }
    @JvmStatic
    @BindingAdapter("acreage")
    fun setAcreage(textView : TextView, acreage : String?){
        acreage?.let {
            val htmlString = "${acreage} m\u00B2"
            textView.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
        }
    }
    @JvmStatic
    @BindingAdapter("isFavorite")
    fun setFavoriteView(imageButton: ImageButton, isFavorite : Boolean?){
        when(isFavorite){
            true -> imageButton.setImageResource(R.drawable.ic_red_favorite)
            else -> imageButton.setImageResource(R.drawable.ic_favorite)
        }
    }
    @JvmStatic
    @BindingAdapter("isSeen")
    fun setSeenStatus(tv : TextView, isSeen : Boolean?){
        isSeen?.let{
            tv.text = if(isSeen) "Đã xem" else "Đã gửi"
            tv.visibility = View.VISIBLE
            return
        }
        tv.visibility = View.GONE
    }
    @JvmStatic
    @BindingAdapter("urlsList","messageImageClickListener")
    fun setMessageImagesRecyclerView(recyclerView: RecyclerView, urlsList : List<String>?, messageImageClickListener : MessageImageClickListener?){
        urlsList?.let {
            val adapter = MessageImageAdapter(it, messageImageClickListener!!)
            recyclerView.adapter = adapter
            recyclerView.visibility = View.VISIBLE
            return
        }
        recyclerView.visibility = View.GONE
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    @BindingAdapter("dateString")
    fun setTimeDifference(textView: TextView, dateString  : String?){
        dateString ?.let {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
            val dateTime = LocalDateTime.parse(it, dateTimeFormatter)
            val now = LocalDateTime.now()
            val duration = Duration.between(dateTime, now)
            val text =if(duration.toMinutes() < 1){
                " . Bây giờ"
            } else if(duration.toHours() < 1){
                val minutes = duration.toMinutes()
                " . $minutes phút"
            } else if(duration.toHours() < 24){
                val hours = duration.toHours()
                " . $hours giờ"
            } else{
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                " . $dayOfWeek"
            }
            textView.text= text
        }
    }
}