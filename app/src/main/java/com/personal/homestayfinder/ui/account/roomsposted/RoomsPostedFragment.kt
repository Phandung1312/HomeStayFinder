package com.personal.homestayfinder.ui.account.roomsposted

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.personal.homestayfinder.adapters.RoomAdapter
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.databinding.RoomsPostedClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomsPostedFragment : BaseFragment<RoomsPostedClass>(RoomsPostedClass::inflate) {
    private val roomsPostedViewModel : RoomsPostedViewModel by viewModels()
    private val args : RoomsPostedFragmentArgs by navArgs()
    private lateinit var itemRVClickListener : ItemRVClickListener
    override fun initView() {
        dataBinding.apply {
            roomsPostedFragment = this@RoomsPostedFragment
            viewModel = roomsPostedViewModel
        }
        dataBinding.rvRooms.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun initListeners() {
        dataBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        itemRVClickListener = object : ItemRVClickListener{
            override fun onClick(view: View, position: Int, item: Any) {
                val roomId = item as String
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
                dataBinding.emptyLayout.visibility = View.GONE
                val adapter = RoomAdapter(rooms.toMutableList(),itemRVClickListener)
                dataBinding.rvRooms.adapter = adapter
            }else{
                dataBinding.emptyLayout.visibility = View.VISIBLE
            }
        }
        if(!TextUtils.equals(currentUser.uid, args.roomMasterId)){
            roomsPostedViewModel.getRoomMasterInfo(args.roomMasterId)
        }
    }
}