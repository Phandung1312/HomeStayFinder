package com.personal.homestayfinder.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.BR
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.common.ItemRoomClickListener
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.databinding.ItemsRoomBinding

class RoomAdapter(
    private val layoutId : Int,
    private var roomsList : MutableList<RoomListItem>,
    private val itemClick : ItemRoomClickListener
) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutId, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = roomsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roomListItem = roomsList[holder.bindingAdapterPosition]
        holder.binding.apply {
            setVariable(BR.item, roomListItem)
            setVariable(BR.itemClick, itemClick)
            executePendingBindings()
        }
    }
}