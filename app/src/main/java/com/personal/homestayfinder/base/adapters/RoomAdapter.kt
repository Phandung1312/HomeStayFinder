package com.personal.homestayfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.databinding.ItemsRoomBinding

class RoomAdapter(
    private var roomsList : MutableList<RoomListItem>,
    private val itemClick : ItemRVClickListener
) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsRoomBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = roomsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roomListItem = roomsList[holder.adapterPosition]
        holder.binding.apply {
            item = roomListItem
            layoutMain.setOnClickListener {
                itemClick.onClick(it,position,roomListItem.id!!)
            }
            executePendingBindings()
        }
    }
}