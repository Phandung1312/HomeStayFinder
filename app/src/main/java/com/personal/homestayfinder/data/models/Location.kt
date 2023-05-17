package com.personal.homestayfinder.data.models

import com.personal.homestayfinder.data.database.entities.CityEntity
import com.personal.homestayfinder.data.database.entities.DistrictEntity
import com.personal.homestayfinder.data.database.entities.WardEntity

class Location(
    val id : Int = 0,
    val name : String = ""
){
    override fun toString(): String {
        return name
    }
}

fun CityEntity.toLocation() : Location{
    return Location(
        id = this.id,
        name = this.cityName
    )
}
fun DistrictEntity.toLocation() : Location{
    return Location(
        id = this.id,
        name = this.districtName
    )
}
fun WardEntity.toLocation() : Location{
    return Location(
        id = this.id,
        name = this.wardName
    )
}