package com.personal.homestayfinder.data.models

class Utility(
    val id : Int? = null,
    val name : String? = null
){
    companion object{
        const val PRIVATE_TOILET = 1
        const val PARKING = 2
        const val WINDOW = 3
        const val SECURITY = 4
        const val WIFI_NETWORK = 5
        const val FREE = 6
        const val OWN_PROPERTY = 7
        const val AIR_CONDITIONING = 8
        const val WATER_HEATER = 9
        const val MEZZANINE = 10
        const val BED = 11
        const val KITCHEN = 12
        const val CLOSET = 13
        const val TELEVISION = 14
        const val PET = 15
        const val BALCONY = 16
        const val FRIDGE = 17
        const val WASHING_MACHINE = 18
    }
}