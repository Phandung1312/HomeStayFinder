package com.personal.homestayfinder.di

import android.content.Context
import androidx.room.Room
import com.personal.homestayfinder.data.database.AppDatabase
import com.personal.homestayfinder.data.database.daos.CityDao
import com.personal.homestayfinder.data.database.daos.DistrictDao
import com.personal.homestayfinder.data.database.daos.WardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext appContext : Context) : AppDatabase{
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Address"
        ).build()
    }
    @Provides
    fun providesCityDao(database: AppDatabase) : CityDao = database.cityDao()

    @Provides
    fun providesDistrictCountyDao(database: AppDatabase) : DistrictDao = database.districtCountyDao()

    @Provides
    fun providesWardCommuneDao(database: AppDatabase) : WardDao = database.wardCommuneDao()
}