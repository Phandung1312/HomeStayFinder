package com.personal.homestayfinder.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.personal.homestayfinder.common.Constant.FAVORITE
import com.personal.homestayfinder.common.Constant.ROOM
import com.personal.homestayfinder.data.models.Favorite
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.data.models.toRoomListItem
import com.personal.homestayfinder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject
import javax.inject.Named

class FavoriteRepository @Inject constructor(
    @Named(FAVORITE)
    private val favorite : CollectionReference,
    @Named(ROOM)
    private val room : CollectionReference,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) {
    fun getAllFavoriteRooms(userId : String) = flow{
        val allRoomId = favorite.whereEqualTo("userId", userId)
            .get().await().toObjects(Favorite::class.java).map { it.roomId!! }
        if(allRoomId.isNotEmpty()){
            val result = room.whereIn("id", allRoomId)
                .get().await().toObjects(Room::class.java).map { it.toRoomListItem() }
            emit(result)
        }
        else{
            emit(arrayListOf())
        }
    }.flowOn(dispatcher)

    fun checkIsFavorite(userId : String, roomId : String) = flow{
        val result = favorite.whereEqualTo("userId", userId).whereEqualTo("roomId", roomId)
            .get().await()
        emit(!result.isEmpty)
    }.flowOn(dispatcher)

    suspend fun addFavorite(userId : String, roomId : String) = flow{
        val newFavorite = Favorite(roomId, userId)
        favorite.document().set(newFavorite).await()
        emit(true)
    }.flowOn(dispatcher)

    suspend fun unFavorite(userId : String,roomId : String) = flow{
        val querySnapshot = favorite.whereEqualTo("userId", userId)
            .whereEqualTo("roomId", roomId).get().await()
        querySnapshot.documents.forEach { document ->
            document.reference.delete()
        }
        emit(false)
    }.flowOn(dispatcher)
}