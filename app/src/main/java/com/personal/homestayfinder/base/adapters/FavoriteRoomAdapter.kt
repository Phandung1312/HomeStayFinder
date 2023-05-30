package com.personal.homestayfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.databinding.ItemsFavoriteRoomBinding

class FavoriteRoomAdapter(
    private val roomsList : MutableList<RoomListItem>,
    private val layoutClick : ItemRVClickListener,
    private val ivbClick : ItemRVClickListener
) : RecyclerView.Adapter<FavoriteRoomAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsFavoriteRoomBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsFavoriteRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roomItem = roomsList[holder.adapterPosition]
        holder.binding.apply {
            item = roomItem
            layoutMain.setOnClickListener {
                layoutClick.onClick(it,position,roomItem.id!!)
            }
            ivbFavorite.setOnClickListener {
                ivbClick.onClick(it,holder.adapterPosition ,roomItem.id!!)
            }
            executePendingBindings()
        }
    }
    override fun getItemCount(): Int = roomsList.size
}