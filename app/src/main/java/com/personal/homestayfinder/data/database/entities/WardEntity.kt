package com.personal.homestayfinder.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.personal.homestayfinder.data.models.Location

@Entity(tableName = "ward",
    foreignKeys = [
        ForeignKey(
            entity = DistrictEntity::class,
            parentColumns = ["id"],
            childColumns = ["district_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class WardEntity(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "district_id") val districtId : Int,
    @ColumnInfo(name = "ward_name") val wardName : String
)
