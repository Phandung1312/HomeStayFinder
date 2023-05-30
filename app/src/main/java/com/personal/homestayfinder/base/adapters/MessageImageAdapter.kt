package com.personal.homestayfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.databinding.ItemsMessageImageBinding
import com.personal.homestayfinder.ui.chat.message.MessageImageClickListener

class MessageImageAdapter(
    private val urlsList : List<String>,
    private val messageImageClickListener: MessageImageClickListener
) : RecyclerView.Adapter<MessageImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsMessageImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsMessageImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = urlsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageUrl = urlsList[position]
        holder.binding.iv.setOnClickListener {
            messageImageClickListener.onClick(urlsList[position])
        }
        holder.binding.executePendingBindings()
    }

}