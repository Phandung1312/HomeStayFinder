package com.personal.homestayfinder.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.data.repositories.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {
    var favoriteRooms = MutableLiveData<List<RoomListItem>>()
    private set
    fun getAllFavorite(userId : String){
        showSmallLoading(true)
        parentJob = viewModelScope.launch(handler) {
            favoriteRepository.getAllFavoriteRooms(userId).collect{
                favoriteRooms.postValue(it)
            }
        }
        registerJobFinish()
    }
    fun unFavorite(userId: String, roomId : String) = liveData(handler){
        favoriteRepository.unFavorite(userId, roomId).collect{
            emit(true)
        }
    }
}