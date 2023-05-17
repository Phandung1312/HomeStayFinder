package com.personal.homestayfinder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.data.models.Utility
import com.personal.homestayfinder.databinding.ItemsUtilityBinding

class UtilityAdapter(
    private val context : Context,
    private val utilitiesList : List<Utility>
) : RecyclerView.Adapter<UtilityAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsUtilityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsUtilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = utilitiesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val utility = utilitiesList[position]
        holder.binding.apply {
            tvUtilityName.text = utility.name
            val imageResourceId = context.resources.getIdentifier("ic_${utility.id}","drawable",context.packageName)
            ivUtility.setImageResource(imageResourceId)
        }
    }
}