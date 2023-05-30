package com.personal.homestayfinder.ui.home.searchroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.adapters.RoomTypeAdapter
import com.personal.homestayfinder.data.models.RoomType
import com.personal.homestayfinder.data.models.RoomType.Companion.APARTMENT
import com.personal.homestayfinder.data.models.RoomType.Companion.DORMITORY
import com.personal.homestayfinder.data.models.RoomType.Companion.ROOM_FOR_RENT
import com.personal.homestayfinder.data.models.RoomType.Companion.SHARED_ROOM
import com.personal.homestayfinder.data.models.RoomType.Companion.WHOLE_HOUSE
import com.personal.homestayfinder.data.models.SearchFilter
import com.personal.homestayfinder.databinding.DialogFragmentFilterBottomSheetBinding

class FilterBottomSheet(
    private val currentSearchFilter: SearchFilter?,
    private val callback: SearchFilterCallBack
) : BottomSheetDialogFragment() {
    private var binding: DialogFragmentFilterBottomSheetBinding? = null
    private lateinit var filterBottomSheetViewModel: FilterBottomSheetViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = viewLifecycleOwner
        filterBottomSheetViewModel = ViewModelProvider(this)[FilterBottomSheetViewModel::class.java]
        filterBottomSheetViewModel.setData(currentSearchFilter)
        setRangeSlider()
        binding!!.filterBottomSheet = this
        binding!!.viewModel = filterBottomSheetViewModel
        return binding!!.root
    }
    fun applyFilter() {
        callback.onSearchFilterChanged(filterBottomSheetViewModel.currentSearchFilter.value)
        this.dismiss()
    }
    private fun setRangeSlider(){
        var minValue = if(currentSearchFilter?.minPrice?.toFloat() != null) currentSearchFilter.minPrice?.toFloat() else {500000f}
        var maxValue = 15000000f
        currentSearchFilter?.maxPrice?.toFloat()?.let {
            maxValue = if(it > 15000000L) 15000000f else it
        }
        binding!!.rangeSlider.setValues(minValue,maxValue)
        binding!!.rangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            filterBottomSheetViewModel.onPriceChanged(values[0].toLong(),values[1].toLong())
        }
    }

    override fun onResume() {
        super.onResume()
        val roomTypes = listOf(
            RoomType(DORMITORY,requireContext().getString(R.string.Dormitory)),
            RoomType(ROOM_FOR_RENT,requireContext().getString(R.string.Room_for_rent)),
            RoomType(SHARED_ROOM,requireContext().getString(R.string.Shared_room)),
            RoomType(WHOLE_HOUSE,requireContext().getString(R.string.Whole_house)),
            RoomType(APARTMENT,requireContext().getString(R.string.Apartment)),
        )
        val adapter = RoomTypeAdapter(binding!!.actRoomType.context,roomTypes.toMutableList())
        binding!!.actRoomType.setAdapter(adapter)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    interface SearchFilterCallBack {
        fun onSearchFilterChanged(searchFilter: SearchFilter?)
    }
}