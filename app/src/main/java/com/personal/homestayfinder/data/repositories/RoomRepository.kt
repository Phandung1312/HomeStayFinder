package com.personal.homestayfinder.data.repositories

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.personal.homestayfinder.common.Constant
import com.personal.homestayfinder.common.Constant.CHAT
import com.personal.homestayfinder.common.Constant.FAVORITE
import com.personal.homestayfinder.common.Constant.ROOM
import com.personal.homestayfinder.common.Constant.ROOM_IMAGE
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.toRoomListItem
import com.personal.homestayfinder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class RoomRepository @Inject constructor(
    @Named(ROOM_IMAGE)
    private val roomImages : StorageReference,
    @Named(CHAT)
    private val chat: DatabaseReference,
    @Named(ROOM)
    private val room : CollectionReference,
    @Named(FAVORITE)
    private val favorite : CollectionReference,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun addRoom(newRoom: Room): Unit = withContext(dispatcher) {
        newRoom.id = room.document().id
        val urlsDeferred: List<Deferred<String>> = newRoom.imagesList.map { uri ->
            async {
                val fileName = UUID.randomUUID().toString()
                val imageRef = roomImages.child(newRoom.id!!).child(fileName)
                imageRef.putFile(uri.toUri()).await()
                val url = imageRef.downloadUrl.await()
                url.toString()
            }
        }
        newRoom.imagesList = urlsDeferred.awaitAll().toMutableList()
        room.document(newRoom.id!!).set(newRoom).await()
    }
    suspend fun updateRoom(newRoom : Room): Unit = withContext(dispatcher){
        val urlsDeferred: List<Deferred<String>> =  newRoom.imagesList.map { image ->
            async {
                if(image.startsWith("http")){
                    image
                }
                else{
                    val uri = Uri.parse(image)
                    val fileName = UUID.randomUUID().toString()
                    val imageRef = roomImages.child(newRoom.id!!).child(fileName)
                    imageRef.putFile(uri).await()
                    val url = imageRef.downloadUrl.await()
                    url.toString()
                }
            }
        }
        newRoom.imagesList = urlsDeferred.awaitAll().toMutableList()
        room.document(newRoom.id!!).set(newRoom).await()
    }
    suspend fun getAllRoom() = flow{
        val result = room.get().await().toObjects(Room::class.java).map { it.toRoomListItem() }
        emit(result)
    }.flowOn(dispatcher)
    suspend fun getRoomById(roomId : String) =flow{
        val result = room.document(roomId).get().await().toObject(Room::class.java)
        emit(result)
    }.flowOn(dispatcher)

    suspend fun removeRoom(roomId: String) = flow{
        roomImages.child(roomId).listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    item.delete()
                }
                roomImages.child(roomId).delete()
            }.await()
        if (favorite.get().await().size() > 0) {
            val favoritesQuerySnapshot = favorite.whereEqualTo("roomId", roomId).get().await()
            for (favoriteDocument in favoritesQuerySnapshot.documents) {
                favoriteDocument.reference.delete().await()
            }
        }
        room.document(roomId).delete().await()
        emit(true)
    }.flowOn(dispatcher)

    suspend fun getRoomsByRoomMasterId(roomMasterId : String) = flow {
        val result = room.whereEqualTo("roomMasterId",roomMasterId).get().await().toObjects(Room::class.java)
            .map { it.toRoomListItem() }
        emit(result)
    }.flowOn(dispatcher)

    suspend fun submitSchedule(message : Message) = flow {
        chat.child(message.sender!!).child(message.receiver!!).push().setValue(message).await()
        chat.child(message.receiver!!).child(message.sender!!).push().setValue(message).await()
        emit(true)
    }.flowOn(dispatcher)
}