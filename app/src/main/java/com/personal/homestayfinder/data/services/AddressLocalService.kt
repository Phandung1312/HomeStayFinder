package com.personal.homestayfinder.data.services

import com.personal.homestayfinder.data.database.daos.CityDao
import com.personal.homestayfinder.data.database.daos.DistrictDao
import com.personal.homestayfinder.data.database.daos.WardDao
import com.personal.homestayfinder.data.database.entities.CityEntity
import com.personal.homestayfinder.data.database.entities.DistrictEntity
import com.personal.homestayfinder.data.database.entities.WardEntity
import javax.inject.Inject

class AddressLocalService @Inject constructor(
    private val cityDao: CityDao,
    private val districtDao: DistrictDao,
    private val wardDao: WardDao
) {
    suspend fun getAllCity() : List<CityEntity>{
        return cityDao.getAll()
    }
    suspend fun getDistrictsByCityId(cityId : Int) : List<DistrictEntity>{
        return districtDao.getDistrictsByCityId(cityId)
    }
    suspend fun getWardsByDistrictId(districtId : Int) : List<WardEntity>{
        return wardDao.getWardsByDistrictId(districtId)
    }
}