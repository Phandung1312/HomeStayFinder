package com.personal.homestayfinder.ui.home

import androidx.lifecycle.liveData
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.toLocation
import com.personal.homestayfinder.data.repositories.AddressRepository
import com.personal.homestayfinder.data.repositories.BannerRepository
import com.personal.homestayfinder.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val bannerRepository: BannerRepository,
    private val addressRepository: AddressRepository
) : BaseViewModel() {
    fun getSearchTrends(cityId: Int) = liveData(handler) {
        roomRepository.getSearchTrends(cityId).collect{
            emit(it)
        }
    }
    fun getRoomsByCityId(cityId : Int) = liveData(handler){
        try {
            roomRepository.getRoomsByCityId(cityId).collect{
                emit(it)
                delay(500)
                showFragmentLoading(false)
            }
        }finally {
            delay(500)
            showFragmentLoading(false)
        }
    }
    fun getAllBanners() = liveData(handler) {
        bannerRepository.getAllBanners().collect{
            emit(it)
        }
    }

    fun getAllCity() = liveData(handler) {
        val result = addressRepository.getAllCity().map { it.toLocation() }
        emit(result)
    }
}