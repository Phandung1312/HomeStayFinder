package com.personal.homestayfinder.activities.main

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.repositories.AddressRepository
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository
) : BaseViewModel() {

    fun changeUserStatus(idUser: String, online: Boolean) {
        parentJob = viewModelScope.launch(handler) {
            userRepository.changeStatus(idUser, online)
        }
    }

    fun getFirstCity() = liveData(handler){
        emit(addressRepository.getFirstCity())
    }

    fun getCityById(cityId : Int) = liveData {
        emit(addressRepository.getCityById(cityId))
    }
}