package com.personal.homestayfinder.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personal.homestayfinder.data.database.entities.DistrictEntity

@Dao
interface DistrictDao {
    @Query("SELECT * FROM district")
    suspend fun getAll() : List<DistrictEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistrictCounty(vararg districts : DistrictEntity)
    @Query("SELECT * FROM district WHERE city_id = :cityId")
    suspend fun getDistrictsByCityId(cityId : Int?) : List<DistrictEntity>
}