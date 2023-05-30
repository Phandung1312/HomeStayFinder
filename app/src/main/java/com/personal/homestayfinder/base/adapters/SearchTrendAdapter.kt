package com.personal.homestayfinder.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personal.homestayfinder.data.models.SearchTrend
import com.personal.homestayfinder.databinding.ItemsSearchTrendBinding

class SearchTrendAdapter(
    private val searchTrendsList : List<SearchTrend>,
    private val itemClick : SearchTrendClickListener
) : RecyclerView.Adapter<SearchTrendAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemsSearchTrendBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsSearchTrendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = searchTrendsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchTrend = searchTrendsList[position]
        holder.binding.searchTrend = searchTrend
        holder.binding.itemClick = itemClick
        holder.binding.executePendingBindings()
    }
}
interface SearchTrendClickListener{
    fun onClick(districtName : String)
}