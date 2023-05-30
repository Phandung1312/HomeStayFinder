package com.personal.homestayfinder.data.repositories

import android.content.Context
import com.personal.homestayfinder.data.services.AddressLocalService
import com.personal.homestayfinder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val addressLocalService: AddressLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getAllCity() = withContext(dispatcher){
        addressLocalService.getAllCity()
    }

    suspend fun getFirstCity() = withContext(dispatcher){
        addressLocalService.getFirstCity()
    }

    suspend fun getCityById(cityId: Int) = withContext(dispatcher){
        addressLocalService.getCityById(cityId)
    }
    suspend fun getDistrictsByCityId(cityId : Int)= withContext(dispatcher){
        addressLocalService.getDistrictsByCityId(cityId)
    }
    suspend fun getWardsByDistrictId(districtId : Int) = withContext(dispatcher){
        addressLocalService.getWardsByDistrictId(districtId)
    }
}