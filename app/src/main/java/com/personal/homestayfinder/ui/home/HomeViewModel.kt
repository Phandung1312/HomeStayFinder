package com.personal.homestayfinder.ui.home

import androidx.lifecycle.liveData
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : BaseViewModel() {

    fun getAllRoom() = liveData(handler){
        try {
            roomRepository.getAllRoom().collect{
                emit(it)
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}