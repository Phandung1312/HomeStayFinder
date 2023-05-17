package com.personal.homestayfinder.activities.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {
    var errorUpdate = MutableLiveData<Boolean>()
    private set
    fun updateUser(newUser : User){
        showScreenLoading(true)
        parentJob = viewModelScope.launch {
            try {
                userRepository.updateUser(newUser)
                errorUpdate.postValue(false)
            }
            catch (e : Exception){
                errorUpdate.postValue(true)
            }
        }
        registerJobFinish()
    }
}