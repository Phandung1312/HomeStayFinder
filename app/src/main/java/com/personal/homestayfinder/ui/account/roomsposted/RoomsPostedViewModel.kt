package com.personal.homestayfinder.ui.account.roomsposted

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.data.repositories.RoomRepository
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsPostedViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {
    var roomMaster = MutableLiveData<User>()
        private set

    fun getRoomsByRoomMasterId(roomMasterId: String) = liveData(handler) {
        showSmallLoading(true)
        try {
            roomRepository.getRoomsByRoomMasterId(roomMasterId).collect {
                emit(it)
            }
        } finally {
            showSmallLoading(false)
        }
    }

    fun getRoomMasterInfo(idUser: String) {
        viewModelScope.launch(handler) {
            userRepository.getUserById(idUser).collect {
                roomMaster.postValue(it)
            }
        }
    }
}