package com.personal.homestayfinder.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.data.models.toLocation
import com.personal.homestayfinder.data.repositories.AddressRepository
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository
) : BaseViewModel() {
    fun getUserById(idUser: String) = liveData(handler) {
        showSmallLoading(true)
        try {
            userRepository.getUserById(idUser).collect { user ->
                emit(user)
            }
        } finally {
            showSmallLoading(false)
        }
    }

    fun signOut(idUser: String) {
        parentJob = viewModelScope.launch(handler) {
            userRepository.changeStatus(idUser, false)
        }
    }
    fun getAllCity() = liveData(handler) {
        val result = addressRepository.getAllCity().map { it.toLocation() }
        emit(result)
    }
}