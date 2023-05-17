package com.personal.homestayfinder.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.homestayfinder.data.database.entities.WardEntity

@Dao
interface WardDao {
    @Query("SELECT * FROM ward")
    suspend fun getAll() : List<WardEntity>
    @Insert
    suspend fun insertWard(vararg wards: WardEntity)
    @Query("SELECT * FROM ward WHERE district_id = :districtId")
    suspend fun getWardsByDistrictId(districtId : Int) : List<WardEntity>
}