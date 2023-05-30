package com.personal.homestayfinder.ui.home.addroom

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.ImageViewModel
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.RoomType
import com.personal.homestayfinder.data.models.Utility
import com.personal.homestayfinder.data.models.Utility.Companion.AIR_CONDITIONING
import com.personal.homestayfinder.data.models.Utility.Companion.BALCONY
import com.personal.homestayfinder.data.models.Utility.Companion.BED
import com.personal.homestayfinder.data.models.Utility.Companion.CLOSET
import com.personal.homestayfinder.data.models.Utility.Companion.FREE
import com.personal.homestayfinder.data.models.Utility.Companion.FRIDGE
import com.personal.homestayfinder.data.models.Utility.Companion.KITCHEN
import com.personal.homestayfinder.data.models.Utility.Companion.MEZZANINE
import com.personal.homestayfinder.data.models.Utility.Companion.OWN_PROPERTY
import com.personal.homestayfinder.data.models.Utility.Companion.PARKING
import com.personal.homestayfinder.data.models.Utility.Companion.PET
import com.personal.homestayfinder.data.models.Utility.Companion.PRIVATE_TOILET
import com.personal.homestayfinder.data.models.Utility.Companion.SECURITY
import com.personal.homestayfinder.data.models.Utility.Companion.TELEVISION
import com.personal.homestayfinder.data.models.Utility.Companion.WASHING_MACHINE
import com.personal.homestayfinder.data.models.Utility.Companion.WATER_HEATER
import com.personal.homestayfinder.data.models.Utility.Companion.WIFI_NETWORK
import com.personal.homestayfinder.data.models.Utility.Companion.WINDOW
import com.personal.homestayfinder.data.models.toLocation
import com.personal.homestayfinder.data.repositories.AddressRepository
import com.personal.homestayfinder.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddRoomViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val roomRepository: RoomRepository) : ImageViewModel() {
    var isHasBeenInit = MutableLiveData(false)
    private set
    var isNewRoom = MutableLiveData(true)
    private set
    private var _currentRoom = MutableLiveData(Room())
    val currentRoom : LiveData<Room> get() = _currentRoom

    var isCompleteSuccess = MutableLiveData<Boolean>()

    private set
    //**************************************************Information**************************************

    private var _roomTypeValidate = MutableLiveData<Boolean?>()
    val roomTypeValidate : LiveData<Boolean?> get() = _roomTypeValidate

    private var _numOfRoomsValidate = MutableLiveData<Boolean?>()
    val numOfRoomsValidate : LiveData<Boolean?> get() = _numOfRoomsValidate

    private var _capacityValidate = MutableLiveData<Boolean?>()
    val capacityValidate : LiveData<Boolean?> get() = _capacityValidate

    private var _genderValidate = MutableLiveData<Boolean?>()
    val genderValidate : LiveData<Boolean?> get() = _genderValidate

    private var _acreageValidate = MutableLiveData<Boolean?>()
    val acreageValidate : LiveData<Boolean?> get() = _acreageValidate

    private var _rentalPriceValidate = MutableLiveData<Boolean?>()
    val rentalPriceValidate : LiveData<Boolean?> get() = _rentalPriceValidate

    private var _depositPriceValidate = MutableLiveData<Boolean?>()
    val depositPriceValidate : LiveData<Boolean?> get() = _depositPriceValidate

    private var _electricPriceValidate = MutableLiveData<Boolean?>()
    val electricPriceValidate : LiveData<Boolean?> get() = _electricPriceValidate

    private var _waterPriceValidate = MutableLiveData<Boolean?>()
    val waterPriceValidate : LiveData<Boolean?> get() = _waterPriceValidate
    fun onRoomTypeSelected(id : Int,name : String){
        _currentRoom.value?.roomType = RoomType(id, name)
        _roomTypeValidate.value = true
    }
    fun onGenderSelected(id: Int) {
        _currentRoom.value?.gender = id
        _genderValidate.value = true
    }
    fun onNumOfRoomsChanged(text: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int){
        text?.let {
            _numOfRoomsValidate.value = it.isNotBlank()
            _currentRoom.value?.numberOfRoom = it.toString().toIntOrNull()
        }
    }
    fun onCapacityChanged(text: CharSequence?,
                          start: Int,
                          before: Int,
                          count: Int){
        text?.let {
            _capacityValidate.value = it.isNotBlank()
            _currentRoom.value?.capacity = it.toString().toIntOrNull()
        }
    }
    fun onAcreageChanged(text: CharSequence?,
                         start: Int,
                         before: Int,
                         count: Int){
        text?.let {
            _acreageValidate.value = it.isNotBlank()
            _currentRoom.value?.acreage = it.toString()
        }
    }
    fun onRentalPriceChanged(text: CharSequence?,
                             start: Int,
                             before: Int,
                             count: Int){
        text?.let {
            _rentalPriceValidate.value = it.isNotBlank()
            _currentRoom.value?.rentalPrice = it.toString().toLongOrNull()
        }
    }
    fun onDepositPriceChanged(text: CharSequence?,
                              start: Int,
                              before: Int,
                              count: Int){
        text?.let {
            _depositPriceValidate.value = it.isNotBlank()
            _currentRoom.value?.depositPrice = it.toString().toLongOrNull()
        }
    }
    fun onElectricPriceChanged(text: CharSequence?,
                               start: Int,
                               before: Int,
                               count: Int){
        text?.let {
            _electricPriceValidate.value = it.isNotBlank()
            _currentRoom.value?.electricPrice = it.toString().toLongOrNull()
        }
    }
    fun onWaterPriceChanged(text: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int){
        text?.let {
            _waterPriceValidate.value = it.isNotBlank()
            _currentRoom.value?.waterPrice= it.toString().toLongOrNull()
        }
    }
    fun onInternetPriceChanged(text: CharSequence?,
                               start: Int,
                               before: Int,
                               count: Int){
        text?.let {
            _currentRoom.value?.internetPrice = it.toString().toLongOrNull()
        }
    }
    fun onStreetNamesChanged(text: CharSequence?,
                             start: Int,
                             before: Int,
                             count: Int){
        text?.let{
            _streetNamesValidate.value = it.isNotBlank()
            _currentRoom.value?.streetNames = it.toString()
        }
    }
    fun onApartmentNumberChanged(text: CharSequence?,
                                 start: Int,
                                 before: Int,
                                 count: Int){
        text?.let{
            _apartmentNumberValidate.value = it.isNotBlank()
            _currentRoom.value?.apartmentNumber = it.toString()
        }
    }
    fun isValidateRoomInfo() : Boolean{
        val fieldsList = listOf(
            _roomTypeValidate,
            _numOfRoomsValidate,
            _capacityValidate,
            _genderValidate,
            _acreageValidate,
            _rentalPriceValidate,
            _depositPriceValidate,
            _electricPriceValidate,
            _waterPriceValidate
        )
        return isValidate(fieldsList)
    }
    //*****************************************************Address**********************************
    private val _cities = MutableLiveData<List<Location>?>()
    val cities : LiveData<List<Location>?> get() = _cities

    private val _districts = MutableLiveData<List<Location>?>()
    val districts : LiveData<List<Location>?> get() = _districts

    private val _wards = MutableLiveData<List<Location>?>()
    val wards : LiveData<List<Location>?> get() = _wards

    private var _cityValidate = MutableLiveData<Boolean?>()
    val cityValidate : LiveData<Boolean?> get() = _cityValidate

    private var _districtValidate = MutableLiveData<Boolean?>()
    val districtValidate : LiveData<Boolean?> get() = _districtValidate

    private var _wardValidate = MutableLiveData<Boolean?>()
    val wardValidate : LiveData<Boolean?> get() = _wardValidate

    private var _streetNamesValidate = MutableLiveData<Boolean?>()
    val streetNamesValidate : LiveData<Boolean?> get() = _streetNamesValidate

    private var _apartmentNumberValidate = MutableLiveData<Boolean?>()
    val apartmentNumberValidate : LiveData<Boolean?> get() = _apartmentNumberValidate

    fun getAllCity(cityId : Int?) {
        parentJob = viewModelScope.launch(handler) {
            val cityEntities = addressRepository.getAllCity()
            _cities.value = (cityEntities.map { it.toLocation() })
            if(_currentRoom.value?.city == null){
                val room = _currentRoom.value
                if(cityId != null){
                    room?.city = _cities.value?.find { it.id == cityId }
                }
                else{
                    room?.city = _cities.value?.get(0)
                }
                _currentRoom.value = room
                _cityValidate.postValue(true)
                val districtEntities = addressRepository.getDistrictsByCityId(cityId = currentRoom.value!!.city!!.id)
                _districts.postValue(districtEntities.map { it.toLocation() })
            }
        }
    }
    fun onCitySelected(location : Any){
        if(location is Location){
            if(location.id != currentRoom.value?.city?.id){
                _districtValidate.postValue(null)
                _wardValidate.postValue(null)
                _currentRoom.value?.district = null
                _currentRoom.value?.ward = null
                parentJob = viewModelScope.launch(handler) {
                    val districtEntities = addressRepository.getDistrictsByCityId(cityId = location.id)
                    _districts.postValue(districtEntities.map { it.toLocation() })
                }
                _currentRoom.value?.city = location
                _currentRoom.postValue(_currentRoom.value)
                _cityValidate.postValue(true)
            }
        }
    }
    fun onDistrictSelected(location : Any){
        if(location is Location){
            if(location.id != currentRoom.value?.district?.id){
                _wardValidate.postValue(null)
                _currentRoom.value?.ward = null
                parentJob = viewModelScope.launch(handler) {
                    val wardEntities = addressRepository.getWardsByDistrictId(districtId = location.id)
                    _wards.postValue(wardEntities.map { it.toLocation() })
                }
                _currentRoom.value?.district = location
                _currentRoom.postValue(_currentRoom.value)
                _districtValidate.postValue(true)
            }
        }
    }
    fun onWardSelected(location : Any){
        if(location is Location){
            _wardValidate.postValue(true)
            _currentRoom.value?.ward = location
            _currentRoom.postValue(_currentRoom.value)
        }
    }
    fun isValidateAddress() : Boolean{
        val fieldsList = listOf(
            _cityValidate,
            _districtValidate,
            _wardValidate,
            _streetNamesValidate,
            _apartmentNumberValidate
        )
        val result = isValidate(fieldsList)
        if(result && _currentRoom.value?.title == null){
            _currentRoom.value?.let {
                it.title = "${it.roomType?.typeName}, ${it.ward}, ${it.district}"
            }
            _titleValidate.postValue(true)
        }
        return result
    }

    //***********************************************Utilities**************************************
    var isVisible = MutableLiveData<Boolean>()
    private var _imagesValidate = MutableLiveData<Boolean?>()
    val imagesValidate : LiveData<Boolean?> get() = _imagesValidate

    private var _utilitiesValidate = MutableLiveData<Boolean?>()
    val utilitiesValidate : LiveData<Boolean?> get() = _utilitiesValidate
    //Checkbox state
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
    fun onUtilitiesCheckedChanged(id : Int,name : String, isChecked : Boolean ,cbState : MutableLiveData<Boolean>){
        if(isChecked){
            if (!currentRoom.value?.utilitiesList?.any { it.id == id }!!) {
                currentRoom.value?.utilitiesList?.add(Utility(id, name))
            }
            _utilitiesValidate.postValue(true)
        }
        else{
            _currentRoom.value?.utilitiesList?.removeIf{ it.id == id}
        }
        cbState.postValue(isChecked)
    }
    override fun updateImagesValidate(){
        imagesList.value?.let{
            if(it.size > 3) _imagesValidate.postValue(true)
            else if(_imagesValidate.value != null) {
                _imagesValidate.postValue(false)
            }
        }
    }
    fun isValidateUtilities() : Boolean{
        _currentRoom.value?.imagesList = imagesList.value?.map { it.toString() }?.toMutableList() ?: ArrayList()
        val fieldsList = listOf(
            _imagesValidate,
            _utilitiesValidate,
        )
        return isValidate(fieldsList)
    }
    //*********************************************Confirmation**************************************

    private var _phoneNumberValidate = MutableLiveData<Boolean?>()
    val phoneNumberValidate : LiveData<Boolean?> = _phoneNumberValidate

    private var _titleValidate = MutableLiveData<Boolean?>()
    val titleValidate : LiveData<Boolean?> = _titleValidate

    private var _contentValidate = MutableLiveData<Boolean?>()
    val contentValidate : LiveData<Boolean?> = _contentValidate

    private var _openTimeValidate = MutableLiveData<Boolean?>()
    val openTimeValidate : LiveData<Boolean?> = _openTimeValidate

    private var _closeTimeValidate = MutableLiveData<Boolean?>()
    val closeTimeValidate : LiveData<Boolean?> = _closeTimeValidate

    fun onPhoneNumberChanged(text: CharSequence?,
                                 start: Int,
                                 before: Int,
                                 count: Int){
        text?.let{
            _phoneNumberValidate.value = it.isNotBlank()
            _currentRoom.value?.phoneNumber = it.toString()
        }
    }
    fun onTitleChanged(text: CharSequence?,
                             start: Int,
                             before: Int,
                             count: Int){
        text?.let{
            _titleValidate.value = it.isNotBlank()
            _currentRoom.value?.title = it.toString()
        }
    }
    fun onContentChanged(text: CharSequence?,
                             start: Int,
                             before: Int,
                             count: Int){
        text?.let{
            _contentValidate.value = it.isNotBlank()
            _currentRoom.value?.content = it.toString()
        }
    }
    fun setOpenTime(timeString : String){
        _openTimeValidate.value = true
        val room = _currentRoom.value
        room?.openTime = timeString
        _currentRoom.postValue(room)
    }
    fun setCloseTime(timeString : String){
        _closeTimeValidate.value = true
        val room = _currentRoom.value
        room?.closeTime = timeString
        _currentRoom.postValue(room)
    }
    fun isValidateConfirmation() : Boolean{
        val fieldsList = listOf(
            _phoneNumberValidate,
            _titleValidate,
            _contentValidate,
            _openTimeValidate,
            _closeTimeValidate
        )
        return isValidate(fieldsList)
    }
    private fun isValidate(fieldsList : List<MutableLiveData<Boolean?>>) : Boolean{
        val hasError = fieldsList.any { it.value == null || it.value == false }
        for (field in fieldsList) {
            if (field.value == null) {
                field.value = false
            }
        }
        return !hasError
    }

    fun addRoom(userId : String) {
        viewModelScope.launch {
            try {
                _currentRoom.value?.roomMasterId = userId
                setDateTimeSubmit()
                roomRepository.addRoom(_currentRoom.value!!)
                isCompleteSuccess.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                isCompleteSuccess.postValue(false)
            }
        }
    }
    private fun setDateTimeSubmit(){
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy")
        _currentRoom.value?.dateSubmitted = dateFormat.format(calendar.time)
    }
    fun setRoomData(roomId : String){
        showSmallLoading(true)
        parentJob = viewModelScope.launch(handler) {
            roomRepository.getRoomById(roomId).collect{room ->
                _currentRoom.value = room!!
                imagesList.value = _currentRoom.value!!.imagesList.map { it.toUri() }.toMutableList()
                setAddressData()
                setUtilities()
                validateAllField()
                isNewRoom.postValue(false)
                isHasBeenInit.postValue(true)
            }
        }
        registerJobFinish()
    }
    fun updateRoom(){
        viewModelScope.launch {
            try {
                roomRepository.updateRoom(_currentRoom.value!!)
                isCompleteSuccess.postValue(true)
            }catch (e : Exception){
                e.printStackTrace()
                isCompleteSuccess.postValue(false)
            }
        }
    }
    private fun validateAllField(){
        val fieldsList = listOf(
            _roomTypeValidate,
            _numOfRoomsValidate,
            _capacityValidate,
            _genderValidate,
            _acreageValidate,
            _rentalPriceValidate,
            _depositPriceValidate,
            _electricPriceValidate,
            _waterPriceValidate,
            _cityValidate,
            _districtValidate,
            _wardValidate,
            _streetNamesValidate,
            _apartmentNumberValidate,
            _imagesValidate,
            _utilitiesValidate,
            _phoneNumberValidate,
            _titleValidate,
            _contentValidate,
            _openTimeValidate,
            _closeTimeValidate
        )
        for (field in fieldsList) {
                field.value = true
        }
    }
    private suspend fun setAddressData(){
            val districtEntities = addressRepository.getDistrictsByCityId(cityId = currentRoom.value!!.city!!.id)
            _districts.postValue(districtEntities.map { it.toLocation() })
            val wardEntities = addressRepository.getWardsByDistrictId(districtId = currentRoom.value!!.district!!.id)
            _wards.postValue(wardEntities.map { it.toLocation() })
    }
    private  fun setUtilities(){
        val stateMap = hashMapOf(
            PRIVATE_TOILET to cbPrivateToiletState,
            PARKING to cbParkingState,
            WINDOW to cbWindowState,
            SECURITY to cbSecurityState,
            WIFI_NETWORK to cbWifiNetworkState,
            FREE to cbFreeState,
            OWN_PROPERTY to cbOwnPropertyState,
            AIR_CONDITIONING to cbAirConditioningState,
            WATER_HEATER to cbWaterHeaterState,
            KITCHEN to cbKitchenState,
            FRIDGE to cbFridgeState,
            WASHING_MACHINE to cbWashingMachineState,
            MEZZANINE to cbMezzanineState,
            BED to cbBedState,
            CLOSET to cbClosetState,
            TELEVISION to cbTelevisionState,
            PET to cbPetState,
            BALCONY to cbBalconyState
        )
        currentRoom.value?.utilitiesList?.forEach { utility ->
            stateMap[utility.id]?.postValue(true)
        }
    }
}