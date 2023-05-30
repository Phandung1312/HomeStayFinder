package com.personal.homestayfinder.ui.home.roomdetails

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.adapters.UtilityAdapter
import com.personal.homestayfinder.base.dialogs.ConfirmDialog
import com.personal.homestayfinder.base.dialogs.ImageSwiperDialog
import com.personal.homestayfinder.base.dialogs.ScheduleDialog
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.Utility
import com.personal.homestayfinder.databinding.RoomDetailsClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomDetailsFragment : BaseFragment<RoomDetailsClass>(RoomDetailsClass::inflate){
    private val roomDetailsViewModel : RoomDetailsViewModel by viewModels()
    private val args : RoomDetailsFragmentArgs by navArgs()
    private lateinit var urlsList : List<String>
    private lateinit var itemClick : ItemRVClickListener
    private lateinit var roomMasterId : String
    private lateinit var roomMasterPhoneNumber : String
    private lateinit var address : String
    private var isFirstLoading = true
    override fun onStart() {
        super.onStart()
        if(isFirstLoading){
            showFragmentLoading(true)
            isFirstLoading = false
        }
    }
    override fun initView() {
        hideBottomNavView()
        binding.apply {
            roomDetailsFragment = this@RoomDetailsFragment
            viewModel = roomDetailsViewModel
            rvRoomImages.layoutManager = GridLayoutManager(requireContext(), 2)
            rvUtilities.layoutManager = GridLayoutManager(requireContext(),4)

        }
    }

    override fun initListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        itemClick = object : ItemRVClickListener{
            override fun onClick(view: View, position: Int, item: Any) {
                val imageSwiperDialog = ImageSwiperDialog(urlsList, position)
                imageSwiperDialog.show(parentFragmentManager, null)
            }
        }
        registerObserverFragmentLoadingEvent(roomDetailsViewModel, viewLifecycleOwner)
        registerAllExceptionEvent(roomDetailsViewModel, viewLifecycleOwner)
    }

    override fun initData() {
        roomDetailsViewModel.getRoomById(args.roomId).observe(viewLifecycleOwner){ room ->
            if(room!= null){
                roomMasterId = room.roomMasterId!!
                roomMasterPhoneNumber = room.phoneNumber!!
                address = room.toAddress()
                roomDetailsViewModel.getRoomMasterInfo(roomMasterId)
                binding.room = room
                setRoomImageAdapter(room)
                setUtilityAdapter(room.utilitiesList)
                urlsList = room.imagesList
                if(TextUtils.equals(currentUser.uid, roomMasterId)){
                    binding.layoutRoomMaster.visibility = View.VISIBLE
                }
                else{
                    binding.layoutGuest.visibility = View.VISIBLE
                }
            }
            else{
                binding.apply {
                    roomInfoLayout.visibility = View.GONE
                    emptyLayout.visibility = View.VISIBLE
                    ibFavorite.visibility = View.GONE
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
        binding.rvRoomImages.adapter = adapter
    }
    private fun setUtilityAdapter(utilitiesList : List<Utility>){
        val adapter = UtilityAdapter(requireContext(), utilitiesList = utilitiesList)
        binding.rvUtilities.adapter = adapter
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
    fun phoneCall(){
        showConfirmMessage(
            "Xác nhận",
            "Bạn có muốn gọi đến số $roomMasterPhoneNumber",
            "GỌI LIỀN",
            "THÔI",
            object : ConfirmDialog.ConfirmCallback{
                override fun negativeAction() {

                }

                override fun positiveAction() {
                    showPermission()
                }

            }
        )
    }
    fun showPermission(){
        Dexter.withContext(activity).withPermission(
            android.Manifest.permission.CALL_PHONE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
               val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$roomMasterPhoneNumber")
                startActivity(intent)
            }
            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(activity,"Bạn chưa cho phép thực hiện cuộc gọi",
                    Toast.LENGTH_SHORT).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }

        }).onSameThread().check()
    }
    fun openGoogleMap(){
        findNavController().navigate(RoomDetailsFragmentDirections.actionRoomDetailsFragmentToRoomLocationFragment(address))
    }

    private fun showRotationalDialogForPermission(){
        AlertDialog.Builder(activity)
            .setMessage("Bật quyền trong cài đặt ứng dụng")
            .setPositiveButton("Vào cài đặt"){_,_->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",activity?.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                catch (e : ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Hủy"){dialog,_->
                dialog.dismiss()
            }.show()
    }
}