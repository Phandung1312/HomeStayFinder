package com.personal.homestayfinder.base.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.base.dialogs.AddressDialog
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.databinding.ItemsAddressBinding

class AddressAdapter(
    private val locationsList : List<Location>,
    private val currentLocation: Location,
    private val dialogCallback : AddressDialog.AddressCallBack
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private var selectedPosition = -1
    inner class ViewHolder(val binding : ItemsAddressBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = locationsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationsList[holder.bindingAdapterPosition]
        holder.binding.apply {
            this.location = location
            if(selectedPosition == -1 && location.id == currentLocation.id){
                rbLocation.isChecked = true
            }else {
                rbLocation.isChecked = selectedPosition == holder.bindingAdapterPosition
            }
            rbLocation.setOnClickListener {
                selectedPosition = holder.bindingAdapterPosition
                dialogCallback.onLocationSelected(location = location)
                notifyDataSetChanged()
            }
        }
        holder.binding.executePendingBindings()
    }
}