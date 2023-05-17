package com.personal.homestayfinder.data.models

data class RoomListItem(
    val id : String? = null,
    val roomType : String? = null,
    val title : String? = null,
    val rentalPrice : Long? = null,
    val imageUrl : String? = null,
    val city : String? = null,
    val district : String? = null,
    val ward : String? = null,
    val streetNames : String? = null,
    val apartmentNumber : String? = null,
    val gender : Int? = null,
    val capacity : Int? = null,
)
fun Room.toRoomListItem() : RoomListItem{
    return RoomListItem(
        id = this.id,
        roomType = this.roomType?.typeName,
        title = this.title,
        rentalPrice = this.rentalPrice,
        imageUrl = this.imagesList[0],
        city = this.city?.name,
        district = this.district?.name,
        ward = this.ward?.name,
        streetNames = this.streetNames,
        apartmentNumber = this.apartmentNumber,
        gender = this.gender,
        capacity = this.capacity
    )
}