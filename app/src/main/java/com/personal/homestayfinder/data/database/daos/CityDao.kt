package com.personal.homestayfinder.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personal.homestayfinder.data.database.entities.CityEntity

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    suspend fun getAll() : List<CityEntity>

    @Query("SELECT * FROM city LIMIT 1")
    suspend fun getFirstCity() : CityEntity

    @Query("SELECT * FROM city WHERE id = :cityId")
    suspend fun getCityById(cityId : Int) : CityEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(vararg cites : CityEntity)
}