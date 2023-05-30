package com.personal.homestayfinder.data.models

data class Room(
    var id : String? = null,
    var roomMasterId : String? = null,
    var roomType: RoomType? = null,
    var numberOfRoom : Int? = null,
    var capacity : Int? = null,
    var gender : Int? = null,
    var acreage : String? = null,
    var rentalPrice : Long? = null,
    var depositPrice : Long? = null,
    var electricPrice : Long? = null,
    var waterPrice : Long? = null,
    var internetPrice : Long? =null,
    var city : Location? = null,
    var district : Location? = null,
    var ward : Location? = null,
    var streetNames : String? = null,
    var apartmentNumber : String? = null,
    var imagesList : MutableList<String> = ArrayList(),
    var utilitiesList : MutableList<Utility> = ArrayList(),
    var phoneNumber : String? = null,
    var title : String? = null,
    var content : String? = null,
    var openTime : String? = null,
    var closeTime : String? = null,
    var dateSubmitted : String? = null
){
    fun toAddress() : String{
        return "$apartmentNumber $streetNames, $ward, $district, $city"
    }
}