package com.personal.homestayfinder.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "district",
        foreignKeys = [
            ForeignKey(
                entity = CityEntity::class,
                parentColumns = ["id"],
                childColumns = ["city_id"],
                onDelete = ForeignKey.CASCADE
            )
        ])
data class DistrictEntity(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "city_id") val cityId : Int,
    @ColumnInfo(name = "district_name") val districtName : String
)