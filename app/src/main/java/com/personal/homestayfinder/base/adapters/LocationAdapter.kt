package com.personal.homestayfinder.base.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.personal.homestayfinder.R
import com.personal.homestayfinder.data.models.Location

class LocationAdapter(
    private val context : Context,
    private val locations : List<Location>
) : ArrayAdapter<Location>(context, R.layout.items_autocompleteview, locations){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val location = getItem(position)
        val textView = view.findViewById<TextView>(R.id.tv_autocomplete)
        textView.text = location?.name
        return view
    }

}