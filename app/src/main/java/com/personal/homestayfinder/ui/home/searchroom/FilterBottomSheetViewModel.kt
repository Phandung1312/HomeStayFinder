package com.personal.homestayfinder.ui.home.searchroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personal.homestayfinder.data.models.RoomType
import com.personal.homestayfinder.data.models.SearchFilter
import com.personal.homestayfinder.data.models.Utility

class FilterBottomSheetViewModel : ViewModel() {
    var currentSearchFilter = MutableLiveData(SearchFilter())
        private set
    var cbPrivateToiletState = MutableLiveData<Boolean>()
        private set
    var cbParkingState = MutableLiveData<Boolean>()
        private set
    var cbWindowState = MutableLiveData<Boolean>()
        private set
    var cbSecurityState = MutableLiveData<Boolean>()
        private set
    var cbWifiNetworkState = MutableLiveData<Boolean>()
        private set
    var cbFreeState = MutableLiveData<Boolean>()
        private set
    var cbOwnPropertyState = MutableLiveData<Boolean>()
        private set
    var cbAirConditioningState = MutableLiveData<Boolean>()
        private set
    var cbWaterHeaterState = MutableLiveData<Boolean>()
        private set
    var cbKitchenState = MutableLiveData<Boolean>()
        private set
    var cbFridgeState = MutableLiveData<Boolean>()
        private set
    var cbWashingMachineState = MutableLiveData<Boolean>()
        private set
    var cbMezzanineState = MutableLiveData<Boolean>()
        private set
    var cbBedState = MutableLiveData<Boolean>()
        private set
    var cbClosetState = MutableLiveData<Boolean>()
        private set
    var cbTelevisionState = MutableLiveData<Boolean>()
        private set
    var cbPetState = MutableLiveData<Boolean>()
        private set
    var cbBalconyState = MutableLiveData<Boolean>()
        private set

    fun setData(searchFilter: SearchFilter?) {
        val newSearchFilter = searchFilter?.let {
            SearchFilter(
                it.minPrice,
                it.maxPrice,
                it.roomType,
                it.utilitiesList.toMutableList(),
                it.capacity,
                it.gender
            )
        }
        currentSearchFilter.value = newSearchFilter
        setUtilities()
    }

    fun onUtilitiesCheckedChanged(
        id: Int,
        name: String,
        isChecked: Boolean,
        cbState: MutableLiveData<Boolean>
    ) {
        if (isChecked) {
            if (!currentSearchFilter.value?.utilitiesList?.any { it.id == id }!!) {
                currentSearchFilter.value?.utilitiesList?.add(Utility(id, name))
            }
        } else {
            currentSearchFilter.value?.utilitiesList?.removeIf { it.id == id }
        }
        cbState.postValue(isChecked)
    }

    fun onGenderSelected(id: Int) {
        currentSearchFilter.value?.gender = id
    }

    fun onPriceChanged(minValue: Long, maxValue: Long) {
        var roundedMinValue = (minValue / 500000) * 500000
        var roundedMaxValue = (maxValue / 500000) * 500000
        if (roundedMaxValue == 15000000L) {
            roundedMaxValue = Long.MAX_VALUE
        }
        val searchFilter = currentSearchFilter.value
        searchFilter?.minPrice = roundedMinValue
        searchFilter?.maxPrice = roundedMaxValue
        currentSearchFilter.value = searchFilter
    }

    fun plusCapacity() {
        val searchFilter = currentSearchFilter.value
        searchFilter?.capacity = searchFilter?.capacity?.plus(1)
        currentSearchFilter.value = searchFilter
    }

    fun minusCapacity() {
        val searchFilter = currentSearchFilter.value
        searchFilter?.capacity = searchFilter?.capacity?.minus(1)
        currentSearchFilter.value = searchFilter
    }

    fun onRoomTypeSelected(roomType: Any) {
        if (roomType is RoomType) {
            if (roomType.id != currentSearchFilter.value?.roomType?.id) {
                val searchFilter = currentSearchFilter.value
                searchFilter?.roomType = roomType
                currentSearchFilter.value = searchFilter
            }
        }
    }

    private fun setUtilities() {
        val stateMap = hashMapOf(
            Utility.PRIVATE_TOILET to cbPrivateToiletState,
            Utility.PARKING to cbParkingState,
            Utility.WINDOW to cbWindowState,
            Utility.SECURITY to cbSecurityState,
            Utility.WIFI_NETWORK to cbWifiNetworkState,
            Utility.FREE to cbFreeState,
            Utility.OWN_PROPERTY to cbOwnPropertyState,
            Utility.AIR_CONDITIONING to cbAirConditioningState,
            Utility.WATER_HEATER to cbWaterHeaterState,
            Utility.KITCHEN to cbKitchenState,
            Utility.FRIDGE to cbFridgeState,
            Utility.WASHING_MACHINE to cbWashingMachineState,
            Utility.MEZZANINE to cbMezzanineState,
            Utility.BED to cbBedState,
            Utility.CLOSET to cbClosetState,
            Utility.TELEVISION to cbTelevisionState,
            Utility.PET to cbPetState,
            Utility.BALCONY to cbBalconyState
        )
        currentSearchFilter.value?.utilitiesList?.forEach { utility ->
            stateMap[utility.id]?.postValue(true)
        }
    }
}