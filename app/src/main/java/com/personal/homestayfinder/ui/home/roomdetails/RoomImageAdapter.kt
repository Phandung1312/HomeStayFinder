package com.personal.homestayfinder.ui.home.roomdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.databinding.ItemsRoomImageBinding

class RoomImageAdapter(
    private val imagesList : MutableList<String>,
    private val itemClick : ItemRVClickListener
) : RecyclerView.Adapter<RoomImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsRoomImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsRoomImageBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if(imagesList.size < 5) return imagesList.size
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            binding.url = imagesList[adapterPosition]
            if(position == 3 && imagesList.size > 4){
                binding.layoutBackground.visibility = View.VISIBLE
                binding.tvNumOfImages.text = "${imagesList.size - 4}+"
                binding.tvNumOfImages.visibility = View.VISIBLE
            }
            binding.layoutMain.setOnClickListener {
                itemClick.onClick(it, position, "")
            }
            binding.executePendingBindings()
        }
    }
}