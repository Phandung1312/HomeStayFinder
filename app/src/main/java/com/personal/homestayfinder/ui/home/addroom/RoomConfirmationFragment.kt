package com.personal.homestayfinder.ui.home.addroom

import android.app.TimePickerDialog
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.databinding.RoomConfirmationClass
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RoomConfirmationFragment : BaseFragment<RoomConfirmationClass>(RoomConfirmationClass::inflate) {
    private val addRoomViewModel : AddRoomViewModel by activityViewModels()
    override fun initView() {
        dataBinding.apply {
            roomConfirmationFragment = this@RoomConfirmationFragment
            viewModel = addRoomViewModel
        }
    }
    override fun initListeners() {
        dataBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun initData() {
    }
    private fun getTimeByFromDialog(callback: (String) -> Unit){
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val dialog = TimePickerDialog(context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val sdf = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
                val formattedDateTime = sdf.format(calendar.time)
                callback(formattedDateTime)
            }, currentHour, currentMinute,false)
        dialog.show()
    }
    fun setOpenTime(){
        getTimeByFromDialog { timeString ->
            addRoomViewModel.setOpenTime(timeString)
        }
    }
    fun setCloseTime(){
        getTimeByFromDialog { timeString ->
            addRoomViewModel.setCloseTime(timeString)
        }
    }
    fun addRoom(){
        if(addRoomViewModel.isValidateConfirmation()){
            //Make Loading Animation Visible
            dataBinding.btnAddRoom.visibility = View.GONE
            dataBinding.layoutLoading.visibility = View.VISIBLE
            dataBinding.loadingView.playAnimation()
            addRoomViewModel.addRoom(currentUser.uid)
            addRoomViewModel.isCompleteSuccess.observe(viewLifecycleOwner){ isSuccess ->
                if(isSuccess) Toast.makeText(activity?.applicationContext,"Thêm phòng thành công",Toast.LENGTH_SHORT).show()
                else Toast.makeText(activity?.applicationContext,"Đã có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                dataBinding.loadingView.pauseAnimation()
                dataBinding.layoutLoading.visibility = View.GONE
                dataBinding.btnAddRoom.visibility = View.VISIBLE
                //Clear data before returning to HomeFragment
                requireActivity().viewModelStore.clear()
                findNavController().popBackStack(R.id.homeFragment, false)
                findNavController().navigate(R.id.homeFragment)
            }
        }

    }
    fun updateRoom(){
        if(addRoomViewModel.isValidateConfirmation()){
            dataBinding.btnUpdateRoom.visibility = View.GONE
            dataBinding.layoutLoading.visibility = View.VISIBLE
            dataBinding.loadingView.playAnimation()
            addRoomViewModel.updateRoom()
            addRoomViewModel.isCompleteSuccess.observe(viewLifecycleOwner){ isSuccess ->
                if(isSuccess) Toast.makeText(activity?.applicationContext,"Phòng đã được cập nhật",Toast.LENGTH_SHORT).show()
                else Toast.makeText(activity?.applicationContext,"Đã có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                dataBinding.loadingView.pauseAnimation()
                dataBinding.layoutLoading.visibility = View.GONE
                dataBinding.btnUpdateRoom.visibility = View.VISIBLE
                //Clear data before returning to HomeFragment
                requireActivity().viewModelStore.clear()
                findNavController().popBackStack(R.id.homeFragment, false)
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }
}