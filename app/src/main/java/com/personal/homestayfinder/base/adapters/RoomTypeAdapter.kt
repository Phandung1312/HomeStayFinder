package com.personal.homestayfinder.base.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.personal.homestayfinder.R
import com.personal.homestayfinder.data.models.RoomType

class RoomTypeAdapter(
    private val context : Context,
    private val roomTypes : List<RoomType>
) : ArrayAdapter<RoomType>(context, R.layout.items_autocompleteview, roomTypes){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val roomType = getItem(position)
        val textView = view.findViewById<TextView>(R.id.tv_autocomplete)
        textView.text = roomType?.typeName
        return view
    }

}