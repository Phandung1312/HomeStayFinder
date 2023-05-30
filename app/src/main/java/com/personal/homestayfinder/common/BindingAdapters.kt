package com.personal.homestayfinder.common

import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.personal.homestayfinder.R
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.base.adapters.LocationAdapter
import com.personal.homestayfinder.base.adapters.MessageImageAdapter
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.RoomType
import com.personal.homestayfinder.ui.chat.message.MessageImageClickListener
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imgMessageUrl")
    fun setImageMessage(iv: ImageView, imgMessageUrl: String?) {
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
    fun setImage(iv: ImageView, imgUrl: String?) {
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
    fun setFullSizeImage(iv: ImageView, imgUrlFullSize: String?) {
        imgUrlFullSize?.let {
            Glide.with(iv.context)
                .load(it)
                .fitCenter()
                .into(iv)
        }
    }

    @JvmStatic
    @BindingAdapter("locations")
    fun setDropDownListItems(view: AutoCompleteTextView, locations: List<Location>?) {
        locations?.let {
            val adapter = LocationAdapter(view.context, locations)
            view.setAdapter(adapter)
        }
    }

    @JvmStatic
    @BindingAdapter("currentLocation")
    fun setItemLocationView(view: AutoCompleteTextView, currentLocation: Location?) {
        if (currentLocation == null) {
            view.text = null
        }
    }

    @JvmStatic
    @BindingAdapter("currentRoomType")
    fun setItemRoomTypeView(view: AutoCompleteTextView, typeName: String?) {
        view.setText(typeName)
    }

    @JvmStatic
    @BindingAdapter("isValidate")
    fun setVisibilityErrorText(textView: TextView, isValidate: Boolean?) {
        isValidate?.let {
            if (!isValidate) {
                textView.visibility = View.VISIBLE
                return
            }
        }
        textView.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter("gender")
    fun setGenderDrawable(textView: TextView, gender: Int?) {
        val drawable = when (gender) {
            Constant.ALL_GENDER -> ContextCompat.getDrawable(
                textView.context,
                R.drawable.male_and_female
            )

            Constant.MALE -> ContextCompat.getDrawable(textView.context, R.drawable.male)
            Constant.FEMALE -> ContextCompat.getDrawable(textView.context, R.drawable.female)
            else -> null
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    @JvmStatic
    @BindingAdapter("rentalPrice")
    fun setRentalPriceText(textView: TextView, rentalPrice: Long?) {
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
    fun setPriceText(textView: TextView, price: Long?) {
        price?.let {
            val formatterPrice = when {
                it >= 1000000000 -> "${it / 1000000000} tỉ"
                it >= 1000000 -> {
                    val million = it / 1000000
                    val thousand = (it % 1000000) / 1000
                    if (thousand > 0) {
                        "$million triệu $thousand ngàn"
                    } else {
                        "$million triệu"
                    }
                }

                else -> "${it / 1000}k"
            }
            textView.text = formatterPrice
            return
        }
        textView.text = "Không có"
    }

    @JvmStatic
    @BindingAdapter("searchPrice")
    fun setSearchPriceText(textView: TextView, price: Long?) {
        price?.let {
            val formatterPrice = when {
                it == Long.MAX_VALUE -> "15 triệu+"
                it >= 1000000000 -> "${it / 1000000000} tỉ"
                it >= 1000000 -> {
                    val million = it / 1000000
                    val thousand = (it % 1000000) / 1000
                    if (thousand > 0) {
                        "$million triệu $thousand ngàn"
                    } else {
                        "$million triệu"
                    }
                }

                else -> "${it / 1000}k"
            }
            textView.text = formatterPrice
            return
        }
        textView.text = "0"
    }

    @JvmStatic
    @BindingAdapter("acreage")
    fun setAcreage(textView: TextView, acreage: String?) {
        acreage?.let {
            val htmlString = "${acreage} m\u00B2"
            textView.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
        }
    }

    @JvmStatic
    @BindingAdapter("isFavorite")
    fun setFavoriteView(imageButton: ImageButton, isFavorite: Boolean?) {
        when (isFavorite) {
            true -> imageButton.setImageResource(R.drawable.ic_red_favorite)
            else -> imageButton.setImageResource(R.drawable.ic_favorite)
        }
    }

    @JvmStatic
    @BindingAdapter("isSeen")
    fun setSeenStatus(tv: TextView, isSeen: Boolean?) {
        isSeen?.let {
            tv.text = if (isSeen) "Đã xem" else "Đã gửi"
            tv.visibility = View.VISIBLE
            return
        }
        tv.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter("urlsList", "messageImageClickListener")
    fun setMessageImagesRecyclerView(
        recyclerView: RecyclerView,
        urlsList: List<String>?,
        messageImageClickListener: MessageImageClickListener?
    ) {
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
    fun setTimeDifference(textView: TextView, dateString: String?) {
        dateString?.let {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
            val dateTime = LocalDateTime.parse(it, dateTimeFormatter)
            val now = LocalDateTime.now()
            val duration = Duration.between(dateTime, now)
            val text = if (duration.toMinutes() < 1) {
                " . Bây giờ"
            } else if (duration.toHours() < 1) {
                val minutes = duration.toMinutes()
                " . $minutes phút"
            } else if (duration.toHours() < 24) {
                val hours = duration.toHours()
                " . $hours giờ"
            } else {
                val dayOfWeek =
                    dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                " . $dayOfWeek"
            }
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter("address")
    fun setAddressText(textView: TextView, address: String?) {
        address?.let {
            val colorText = "<font color='#333333'>$it</font>" +
                    "<font color='#455DF6'>. Chỉ đường</font>"
            textView.text = Html.fromHtml(colorText, Html.FROM_HTML_MODE_LEGACY)
        }
    }

    @JvmStatic
    @BindingAdapter("areaName")
    fun setAbbreviatedAreaText(appCompatButton: AppCompatButton, areaName: String?) {
        areaName?.let {
            val words = it.split(" ")
            val initials = StringBuilder()

            for (word in words) {
                if (word.isNotEmpty()) {
                    initials.append(word[0])
                }
            }
            appCompatButton.text = initials.toString().uppercase()
        }
    }

    @JvmStatic
    @BindingAdapter("message", "userId", requireAll = true)
    fun setFontTextView(textView: TextView, message: Message?, userId: String?) {
        message?.let {
            val typeface = if (TextUtils.equals(
                    message.sender,
                    userId
                ) && message.seen == false
            ) ResourcesCompat.getFont(textView.context, R.font.app_font_bold)
            else ResourcesCompat.getFont(textView.context, R.font.app_font_regular)
            textView.typeface = typeface
        }
    }
}