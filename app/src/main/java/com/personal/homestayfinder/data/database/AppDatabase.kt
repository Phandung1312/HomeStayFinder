package com.personal.homestayfinder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.homestayfinder.data.database.daos.CityDao
import com.personal.homestayfinder.data.database.daos.DistrictDao
import com.personal.homestayfinder.data.database.daos.WardDao
import com.personal.homestayfinder.data.database.entities.CityEntity
import com.personal.homestayfinder.data.database.entities.DistrictEntity
import com.personal.homestayfinder.data.database.entities.WardEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CityEntity::class,DistrictEntity::class,WardEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao() : CityDao
    abstract fun districtCountyDao() : DistrictDao
    abstract fun  wardCommuneDao() : WardDao
}