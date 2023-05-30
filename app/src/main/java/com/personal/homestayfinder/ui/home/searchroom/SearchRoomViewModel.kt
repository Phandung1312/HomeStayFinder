package com.personal.homestayfinder.ui.home.searchroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.SearchFilter
import com.personal.homestayfinder.data.models.toLocation
import com.personal.homestayfinder.data.repositories.AddressRepository
import com.personal.homestayfinder.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class SearchRoomViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val roomRepository: RoomRepository
) :BaseViewModel() {
    var currentSearchFilter : SearchFilter? = SearchFilter()
        private set

    fun getAllCity() = liveData(handler) {
        val result = addressRepository.getAllCity().map { it.toLocation() }
        emit(result)
    }
    fun searchRoom(searchAddress : String, cityId : Int) =liveData(handler){
        roomRepository.searchRoom(searchAddress, currentSearchFilter,cityId).collect{
            emit(it)
            showSmallLoading(false)
        }
    }

    fun setFilter(searchFilter: SearchFilter?){
        currentSearchFilter = searchFilter
    }

}