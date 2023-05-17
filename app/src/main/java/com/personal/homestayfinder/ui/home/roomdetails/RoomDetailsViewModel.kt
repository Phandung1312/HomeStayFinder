package com.personal.homestayfinder.ui.home.roomdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.data.models.Schedule
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.data.models.toRoomListItem
import com.personal.homestayfinder.data.repositories.FavoriteRepository
import com.personal.homestayfinder.data.repositories.RoomRepository
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {
    var roomMaster = MutableLiveData<User>()
        private set
    var isFavorite = MutableLiveData<Boolean>()
        private set
    var schedule = MutableLiveData(Schedule())
        private set
    var fullNameValidate = MutableLiveData<Boolean?>()
        private set
    var phoneNumberValidate = MutableLiveData<Boolean?>()
        private set
    var dateTimeValidate = MutableLiveData<Boolean?>()
        private set
    var isScheduleSuccessful = MutableLiveData<Boolean>()
    private  var roomScheduled : RoomListItem? = null
    fun getRoomById(roomId: String) = liveData(handler) {
        roomRepository.getRoomById(roomId).collect {
            emit(it)
            roomScheduled = it?.toRoomListItem()
            setAddressSchedule(it)
            delay(500)
            showFragmentLoading(false)
        }
    }
    private fun setAddressSchedule(room : Room?){
        val tempSchedule = schedule.value
        tempSchedule?.address = "${room?.apartmentNumber} ${room?.streetNames}, ${room?.ward}, ${room?.district}, ${room?.city}"
        schedule.postValue(tempSchedule)
    }
    fun getRoomMasterInfo(idUser: String) {
        viewModelScope.launch(handler) {
            userRepository.getUserById(idUser).collect {
                roomMaster.postValue(it)
            }
        }
    }

    fun checkIsFavorite(userId: String, roomId: String) {
        viewModelScope.launch(handler) {
            favoriteRepository.checkIsFavorite(userId, roomId).collect {
                isFavorite.postValue(it)
            }
        }
    }

    fun changeFavoriteStatus(userId: String, roomId: String) {
        viewModelScope.launch(handler) {
            isFavorite.value?.let {
                if (it) {
                    favoriteRepository.unFavorite(userId, roomId).collect { result ->
                        isFavorite.postValue(result)
                    }
                } else {
                    favoriteRepository.addFavorite(userId, roomId).collect { result ->
                        isFavorite.postValue(result)
                    }
                }
            }
        }
    }
    fun removeRoom(roomId: String) = liveData(handler) {
        roomRepository.removeRoom(roomId).collect {
            emit(it)
        }
    }
    fun onFullNameChanged(text: CharSequence?,
                          start: Int,
                          before: Int,
                          count: Int){
        text?.let {
            fullNameValidate.value = it.isNotBlank()
            schedule.value?.fullName = it.toString()
        }
    }
    fun onPhoneNumberChanged(text: CharSequence?,
                          start: Int,
                          before: Int,
                          count: Int){
        text?.let {
            phoneNumberValidate.value = it.isNotBlank()
            schedule.value?.phoneNumber = it.toString()
        }
    }
    fun setTimeSchedule(time : String){
        dateTimeValidate.value = true
        val tempSchedule = schedule.value
        tempSchedule?.dateTime = time
        schedule.postValue(tempSchedule)
    }
    fun submitSchedule(currentUserId : String) {
        viewModelScope.launch {
            try {
                val contentMessage = "Bạn ơi mình thuê phòng này nhé!\n"+
                    "- Địa chỉ phòng: ${schedule.value?.address}\n" +
                        "-Tên của mình: ${schedule.value?.fullName}\n" +
                        "-Số điện thoại mình: ${schedule.value?.phoneNumber}\n" +
                        "-Thời gian xem phòng: ${schedule.value?.dateTime}"
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy")
                val timeSubmit = dateFormat.format(calendar.time)

                val message = Message(
                    currentUserId,
                    roomMaster.value?.userId,
                    contentMessage,
                    null,
                    timeSubmit,
                    false,
                    roomScheduled
                )
                roomRepository.submitSchedule(message).collect{
                    isScheduleSuccessful.postValue(it)
                }

            }catch (e : Exception){
                e.printStackTrace()
                isScheduleSuccessful.postValue(false)
            }
        }
    }
    fun isValidateSchedule() : Boolean{
        val fieldsList = listOf(
            fullNameValidate,
            phoneNumberValidate,
            dateTimeValidate
        )
        return isValidate(fieldsList)
    }
    fun resetSchedule(){
        schedule.value?.fullName = null
        schedule.value?.phoneNumber = null
        schedule.value?.dateTime = null
        val fieldsList = listOf(
            fullNameValidate,
            phoneNumberValidate,
            dateTimeValidate
        )
        for (field in fieldsList) {
                field.value = null
        }
    }
    private fun isValidate(fieldsList : List<MutableLiveData<Boolean?>>) : Boolean{
        val hasError = fieldsList.any { it.value == null || it.value == false }
        for (field in fieldsList) {
            if (field.value == null) {
                field.value = false
            }
        }
        return !hasError
    }
}