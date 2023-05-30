package com.personal.homestayfinder.ui.home.searchroom

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.adapters.RoomAdapter
import com.personal.homestayfinder.base.dialogs.AddressDialog
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRoomClickListener
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.data.models.SearchFilter
import com.personal.homestayfinder.databinding.SearchRoomClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRoomFragment : BaseFragment<SearchRoomClass>(SearchRoomClass::inflate) {
    private val args : SearchRoomFragmentArgs by navArgs()
    private val searchRoomViewModel: SearchRoomViewModel by viewModels()
    private lateinit var citiesList: List<Location>
    private lateinit var currentArea : Location
    private lateinit var itemClick : ItemRoomClickListener
    private var roomsList : List<RoomListItem> = arrayListOf()
    override fun initView() {
        hideBottomNavView()
        binding.searchRoomFragment = this@SearchRoomFragment
        getCurrentArea().observe(viewLifecycleOwner){
            binding.areaName = it.name
            currentArea = it
        }
        if(roomsList.isNotEmpty()) setRoomAdapter()
    }

    override fun initListeners() {
        binding.edSearch.setOnEditorActionListener{ _,actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                val searchAddress = binding.edSearch.text.toString()
                searchRoom(searchAddress)
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.edSearch.windowToken, 0)
                true
            }else{
                false
            }
        }
        itemClick = object : ItemRoomClickListener{
            override fun onRoomClick(room: RoomListItem) {
                findNavController().navigate(SearchRoomFragmentDirections.actionSearchRoomFragmentToRoomDetailsFragment(room.id!!))
            }

        }
        registerObserverSmallLoadingEvent(searchRoomViewModel,viewLifecycleOwner)
        registerAllExceptionEvent(searchRoomViewModel, viewLifecycleOwner)
    }
    private fun searchRoom(searchAddress : String){
        showSmallLoading(true)
        binding.tvEmpty.visibility = View.GONE
        binding.searchResultLayout.visibility = View.GONE
        searchRoomViewModel.searchRoom(searchAddress,currentArea.id).observe(viewLifecycleOwner){ result ->
            if(result.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
                binding.searchResultLayout.visibility = View.GONE
            }
            else{
                roomsList = result
                setRoomAdapter()
            }
        }
    }
    private fun setRoomAdapter(){
        val adapter = RoomAdapter(
            R.layout.items_search_room,
            roomsList.toMutableList(),
            itemClick
        )
        binding.rvRooms.adapter = adapter
        binding.numOfRooms = roomsList.size.toString()
        binding.tvEmpty.visibility = View.GONE
        binding.searchResultLayout.visibility = View.VISIBLE
    }
    override fun initData() {
            binding.edSearch.apply {
                if(args.districtName != null) {
                    setText(args.districtName)
                }
                requestFocus()
                post {
                    if(this.requestFocus()){
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(this,InputMethodManager.SHOW_IMPLICIT)
                    }
                }
        }
        searchRoomViewModel.getAllCity().observe(viewLifecycleOwner) {
            citiesList = it
        }
    }

    fun showBottomSheet() {
        FilterBottomSheet(
            searchRoomViewModel.currentSearchFilter,
        object : FilterBottomSheet.SearchFilterCallBack{
            override fun onSearchFilterChanged(searchFilter: SearchFilter?) {
                searchRoomViewModel.setFilter(searchFilter)
                val searchAddress = binding.edSearch.text.toString()
                if(searchAddress.isNotBlank()) searchRoom(searchAddress)
            }

        }).show(requireActivity().supportFragmentManager, "BottomSheetDialog")
    }

    fun onAreaChanged() {
        val addressDialog = AddressDialog(
            requireContext(),
            citiesList,
            currentArea,
            fragmentCallback = object : AddressDialog.AddressCallBack {
                override fun onLocationSelected(location: Location) {
                    updateArea(location)
                    binding.areaName = location.name
                }
            },
            "Chuyển đổi khu vực tìm kiếm",
            "Chuyển",
            "Hủy"
        )
        addressDialog.show()
    }

    fun goBack() {
        findNavController().popBackStack()
    }
}