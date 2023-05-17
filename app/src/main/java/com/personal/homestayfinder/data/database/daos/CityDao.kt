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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(vararg cites : CityEntity)
}