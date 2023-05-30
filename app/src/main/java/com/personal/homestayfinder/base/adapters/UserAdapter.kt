package com.personal.homestayfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.databinding.ItemsUserChatBinding
import com.personal.homestayfinder.ui.chat.userchats.UserChatsViewModel

class UserAdapter(
    private val usersList : List<User> = mutableListOf(),
    private val itemClickListener : ItemRVClickListener,
    private val viewModel : UserChatsViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val ownerId : String
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsUserChatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsUserChatBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val user = usersList[bindingAdapterPosition]
            binding.user =user
            binding.layoutMain.setOnClickListener { view ->
                itemClickListener.onClick(view,position, user)
            }
            viewModel.getLastMessage(ownerId, user.userId!!).observe(lifecycleOwner){
                binding.message = it
            }
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = usersList.size
}