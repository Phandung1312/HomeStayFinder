package com.personal.homestayfinder.base.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.base.viewmodel.ImageViewModel
import com.personal.homestayfinder.databinding.ItemsImageBinding

class ImageAdapter(
    private val viewModel : ImageViewModel,
    private var urisList : MutableList<Uri>
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.imagesList.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.imageUri = viewModel.imagesList.value!![bindingAdapterPosition]
            binding.executePendingBindings()
            binding.ivDelete.setOnClickListener {
                urisList.removeAt(bindingAdapterPosition)
                updateList()
            }
        }
    }
    fun updateList(){
        viewModel.imagesList.value = urisList
        viewModel.updateImagesValidate()
        notifyDataSetChanged()
    }
}