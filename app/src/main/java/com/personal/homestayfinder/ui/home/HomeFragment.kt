package com.personal.homestayfinder.ui.home

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.adapters.RoomAdapter
import com.personal.homestayfinder.base.adapters.SearchTrendAdapter
import com.personal.homestayfinder.base.adapters.SearchTrendClickListener
import com.personal.homestayfinder.base.dialogs.AddressDialog
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.common.ItemRoomClickListener
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.databinding.HomeClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeClass>(HomeClass::inflate),ItemRoomClickListener  {
    private val homeViewModel : HomeViewModel by viewModels()
    private lateinit var citiesList : List<Location>
    private lateinit var currentArea : Location
    private var isFirstLoading = true
    override fun onStart() {
        super.onStart()
        if(isFirstLoading){
            showFragmentLoading(true)
            isFirstLoading = false
        }
    }
    override fun initView() {
        showBottomNavView()
        binding.rvRooms.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSearchTrends.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.homeFragment = this@HomeFragment
    }

    override fun initListeners() {
        binding.ivAddRoom.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRoomInformationFragment(null))
        }
        registerObserverFragmentLoadingEvent(homeViewModel, viewLifecycleOwner)
    }

    override fun initData() {
        homeViewModel.getAllBanners().observe(viewLifecycleOwner){ slideModelsList ->
           binding.imageSlider.setImageList(slideModelsList,ScaleTypes.CENTER_CROP)
        }
        getCurrentArea().observe(viewLifecycleOwner){
            currentArea = it
            binding.areaName = it.name
            homeViewModel.getSearchTrends(it.id).observe(viewLifecycleOwner){ searchTrendsList ->
                if(searchTrendsList.isNotEmpty()){
                    val searchTrendAdapter = SearchTrendAdapter(searchTrendsList,
                    object : SearchTrendClickListener{
                        override fun onClick(districtName: String) {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchRoomFragment(districtName))
                        }

                    })
                    binding.rvSearchTrends.adapter = searchTrendAdapter
                }

            }
            homeViewModel.getRoomsByCityId(it.id).observe(viewLifecycleOwner){ roomsList ->
                binding.isEmptyRooms = roomsList.isEmpty()
                if(roomsList.isNotEmpty()){
                    val roomAdapter = RoomAdapter(
                        R.layout.items_room,
                        roomsList.toMutableList(),
                        this
                    )
                    binding.rvRooms.adapter = roomAdapter
                }
            }
        }
        homeViewModel.getAllCity().observe(viewLifecycleOwner){
            citiesList = it
        }
    }


    fun onAreaChanged(){
        val addressDialog = AddressDialog(
            requireContext(),
            citiesList,
            currentArea,
            fragmentCallback = object : AddressDialog.AddressCallBack{
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
    fun goToSearchRoomScreen(){
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchRoomFragment(null))
    }
    override fun onPause() {
        super.onPause()
        showFragmentLoading(false)
    }

    override fun onRoomClick(room: RoomListItem) {
        val roomId = room.id!!
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRoomDetailsFragment(roomId))
    }
}