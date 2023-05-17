package com.personal.homestayfinder.ui.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.personal.homestayfinder.R
import com.personal.homestayfinder.adapters.RoomAdapter
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.databinding.HomeClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeClass>(HomeClass::inflate),ItemRVClickListener  {
    private val homeViewModel : HomeViewModel by viewModels()
    override fun initView() {
        showBottomNavView()
        val list = ArrayList<SlideModel>()
        list.add(SlideModel("https://th.bing.com/th/id/R.d1ad1c3f5250d1fd8d3d44aecb83de8f?rik=ERCA8rp%2bCBn62g&pid=ImgRaw&r=0",ScaleTypes.CENTER_CROP))
        list.add(SlideModel("https://thietkenhadepmoi.com/wp-content/uploads/2020/11/phong-tro-dep-2023.jpg",ScaleTypes.CENTER_CROP))
        list.add(SlideModel("https://th.bing.com/th/id/R.be2901e0f62568d7787a21c07540b595?rik=fu2gkbWNlqzBEQ&riu=http%3a%2f%2fsunshinegroup.vn%2fwp-content%2fuploads%2f2019%2f12%2fthoi-dai-can-ho-1-1024x652.jpg&ehk=4nvlkcqV%2b3kKpTJWeOJaze4G8LxOznxnBZZxQevKbDo%3d&risl=&pid=ImgRaw&r=0",ScaleTypes.CENTER_CROP))
        dataBinding.imageSlider.setImageList(list,ScaleTypes.CENTER_CROP)
        dataBinding.rvRooms.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun initListeners() {
        dataBinding.ivAddRoom.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRoomInformationFragment(null))
        }
    }

    override fun initData() {
        homeViewModel.getAllRoom().observe(viewLifecycleOwner){ roomsList ->
            if(roomsList.isNotEmpty()){
                val adapter = RoomAdapter(
                    roomsList.toMutableList(),
                    this
                )
                dataBinding.rvRooms.adapter = adapter
            }
        }
    }

    override fun onClick(view: View,position : Int, item: Any) {
        val roomId = item as String
        showFragmentLoading(true)
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRoomDetailsFragment(roomId))

    }
}