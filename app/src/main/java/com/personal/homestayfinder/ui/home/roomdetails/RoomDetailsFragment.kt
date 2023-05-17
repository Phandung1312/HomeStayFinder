package com.personal.homestayfinder.ui.home.roomdetails

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.personal.homestayfinder.R
import com.personal.homestayfinder.adapters.UtilityAdapter
import com.personal.homestayfinder.base.dialogs.ConfirmDialog
import com.personal.homestayfinder.base.dialogs.ImageSwiperDialog
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.Utility
import com.personal.homestayfinder.databinding.RoomDetailsClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomDetailsFragment : BaseFragment<RoomDetailsClass>(RoomDetailsClass::inflate)  {
    private val roomDetailsViewModel : RoomDetailsViewModel by viewModels()
    private val args : RoomDetailsFragmentArgs by navArgs()
    private lateinit var urlsList : List<String>
    private lateinit var itemClick : ItemRVClickListener
    private lateinit var roomMasterId : String
    override fun initView() {
        hideBottomNavView()
        dataBinding.apply {
            roomDetailsFragment = this@RoomDetailsFragment
            viewModel = roomDetailsViewModel
            rvRoomImages.layoutManager = GridLayoutManager(requireContext(), 2)
            rvUtilities.layoutManager = GridLayoutManager(requireContext(),4)
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun initListeners() {
        itemClick = object : ItemRVClickListener{
            override fun onClick(view: View, position: Int, item: Any) {
                val imageSwiperDialog = ImageSwiperDialog(urlsList, position)
                imageSwiperDialog.show(parentFragmentManager, null)
            }
        }
        registerObserverFragmentLoadingEvent(roomDetailsViewModel, viewLifecycleOwner)
    }

    override fun initData() {
        roomDetailsViewModel.getRoomById(args.roomId).observe(viewLifecycleOwner){ room ->
            if(room!= null){
                roomMasterId = room.roomMasterId!!
                roomDetailsViewModel.getRoomMasterInfo(roomMasterId)
                dataBinding.room = room
                setRoomImageAdapter(room)
                setUtilityAdapter(room.utilitiesList)
                urlsList = room.imagesList
                if(TextUtils.equals(currentUser.uid, roomMasterId)){
                    dataBinding.layoutRoomMaster.visibility = View.VISIBLE
                }
                else{
                    dataBinding.layoutGuest.visibility = View.VISIBLE
                }
            }
        }
        roomDetailsViewModel.checkIsFavorite(userId = currentUser.uid, roomId = args.roomId)
    }
    fun createSchedule(){
        val scheduleDialog = ScheduleDialog(requireContext(),
            roomDetailsViewModel,
            viewLifecycleOwner,
            currentUserId = currentUser.uid)
        scheduleDialog.show()
        roomDetailsViewModel.isScheduleSuccessful.observe(viewLifecycleOwner){isSuccessful ->
            if(isSuccessful) Toast.makeText(context,"Đặt lịch hẹn thành công, vào tin nhắn để xem chi tiết",Toast.LENGTH_SHORT).show()
            else Toast.makeText(context,"Đã xảy ra lỗi",Toast.LENGTH_SHORT).show()
        }
    }
    fun goToRoomsPosted(){
        findNavController().navigate(RoomDetailsFragmentDirections.actionRoomDetailsFragmentToFragmentRoomsPosted(roomMasterId))
    }
    fun goToMessageScreen(){
        findNavController().navigate(RoomDetailsFragmentDirections.actionRoomDetailsFragmentToMessageFragment(roomMasterId))
    }
    private fun setRoomImageAdapter(room : Room){
        val adapter = RoomImageAdapter(room.imagesList, itemClick)
        dataBinding.rvRoomImages.adapter = adapter
    }
    private fun setUtilityAdapter(utilitiesList : List<Utility>){
        val adapter = UtilityAdapter(requireContext(), utilitiesList = utilitiesList)
        dataBinding.rvUtilities.adapter = adapter
    }
    fun onButtonFavoriteClick(){
        roomDetailsViewModel.changeFavoriteStatus(currentUser.uid, args.roomId)
    }
    fun editRoom(){
        findNavController().navigate(RoomDetailsFragmentDirections.actionRoomDetailsFragmentToRoomInformationFragment(args.roomId))
    }
    fun removeRoom(){
        showConfirmMessage(
            title = R.string.confirm_note,
            message = R.string.confirm_remove,
            positiveTitleResourceId = R.string.confirm_agree,
            negativeTitleResourceId = R.string.confirm_disagree,
            callback = object : ConfirmDialog.ConfirmCallback{
                override fun negativeAction() {

                }

                override fun positiveAction() {
                    roomDetailsViewModel.removeRoom(args.roomId).observe(viewLifecycleOwner){
                        Toast.makeText(context, "Xóa phòng thành công", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }

            }
        )
    }
}