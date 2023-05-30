package com.personal.homestayfinder.ui.account.roomsposted

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.adapters.RoomAdapter
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.common.ItemRoomClickListener
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.databinding.RoomsPostedClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomsPostedFragment : BaseFragment<RoomsPostedClass>(RoomsPostedClass::inflate) {
    private val roomsPostedViewModel : RoomsPostedViewModel by viewModels()
    private val args : RoomsPostedFragmentArgs by navArgs()
    private lateinit var itemRVClickListener : ItemRoomClickListener
    override fun initView() {
        hideBottomNavView()
        binding.apply {
            roomsPostedFragment = this@RoomsPostedFragment
            viewModel = roomsPostedViewModel
        }
        binding.rvRooms.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun initListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        itemRVClickListener = object : ItemRoomClickListener{
            override fun onRoomClick(room: RoomListItem) {
                val roomId = room.id!!
                findNavController().navigate(RoomsPostedFragmentDirections
                    .actionRoomsPostedFragmentToRoomDetailsFragment(roomId))
            }


        }
        registerAllExceptionEvent(roomsPostedViewModel,viewLifecycleOwner)
        registerObserverSmallLoadingEvent(roomsPostedViewModel,viewLifecycleOwner)
    }

    override fun initData() {
        roomsPostedViewModel.getRoomsByRoomMasterId(args.roomMasterId).observe(viewLifecycleOwner){ rooms ->
            if(rooms.isNotEmpty()){
                binding.tvTitle.visibility = View.VISIBLE
                binding.emptyLayout.visibility = View.GONE
                val adapter = RoomAdapter(R.layout.items_room,rooms.toMutableList(),itemRVClickListener)
                binding.rvRooms.adapter = adapter
            }else{
                binding.emptyLayout.visibility = View.VISIBLE
            }
        }
        if(!TextUtils.equals(currentUser.uid, args.roomMasterId)){
            roomsPostedViewModel.getRoomMasterInfo(args.roomMasterId)
        }
    }
}