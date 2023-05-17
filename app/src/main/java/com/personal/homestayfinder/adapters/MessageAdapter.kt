package com.personal.homestayfinder.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.personal.homestayfinder.BR
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.databinding.ItemsMessageLeftBinding
import com.personal.homestayfinder.databinding.ItemsMessageRightBinding
import com.personal.homestayfinder.ui.chat.message.ItemChangeListener
import com.personal.homestayfinder.ui.chat.message.MessageImageClickListener
import com.personal.homestayfinder.ui.chat.message.RoomScheduledClickListener

class MessageAdapter(
    options : FirebaseRecyclerOptions<Message>,
    private val sender : User,
    private val receiver : User,
    private val itemChangeListener : ItemChangeListener,
    private val messageImageClickListener: MessageImageClickListener,
    private val roomScheduledClickListener: RoomScheduledClickListener
) : FirebaseRecyclerAdapter<Message, MessageAdapter.ViewHolder>(options) {
    companion object{
        const val SENDER_TYPE = 0
        const val RECEIVER_TYPE = 1
    }
    inner class ViewHolder(val binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == SENDER_TYPE){
            val binding = ItemsMessageRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else{
            val binding = ItemsMessageLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Message) {
        holder.binding.apply {
            setVariable(BR.messageImageClickListener, messageImageClickListener)
            setVariable(BR.message, model)
            if(model.sender.equals(sender.userId)){
                setVariable(BR.avatarUrl, sender.imgUrl)
            }
            else{
                setVariable(BR.avatarUrl, receiver.imgUrl)
            }
            if(position == itemCount - 1){
                setVariable(BR.isSeen, model.seen)
                if(model.sender.equals(receiver.userId)){
                    itemChangeListener.seen(model)
                }
            }
            else{
                setVariable(BR.isSeen, null)
            }
            setVariable(BR.roomScheduledClickListener, roomScheduledClickListener)
            setVariable(BR.item, model.roomScheduled)
            executePendingBindings()
        }
    }

    override fun onDataChanged() {
        super.onDataChanged()
        itemChangeListener.onScrollRecyclerView(itemCount)
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return if(TextUtils.equals(sender.userId,getItem(position).sender)){
            SENDER_TYPE
        }else{
            RECEIVER_TYPE
        }
    }
}