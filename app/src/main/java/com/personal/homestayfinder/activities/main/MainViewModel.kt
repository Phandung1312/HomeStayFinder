package com.personal.homestayfinder.activities.main

import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository): BaseViewModel() {

    fun changeUserStatus(idUser : String, online : Boolean){
        parentJob = viewModelScope.launch(handler) {
            userRepository.changeStatus(idUser, online)
        }
    }
}