package com.personal.homestayfinder.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.databinding.ItemsImageSwiperBinding

class ImageSwiperAdapter(
    private val urlsList : List<String>
) : RecyclerView.Adapter<ImageSwiperAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemsImageSwiperBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =  ItemsImageSwiperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = urlsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageUrl = urlsList[position]
        holder.binding.executePendingBindings()
    }

}