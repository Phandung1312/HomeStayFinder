package com.personal.homestayfinder.data.repositories

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.personal.homestayfinder.common.Constant.ALL_GENDER
import com.personal.homestayfinder.common.Constant.CHAT
import com.personal.homestayfinder.common.Constant.ROOM
import com.personal.homestayfinder.common.Constant.ROOM_IMAGE
import com.personal.homestayfinder.common.Constant.SEARCH_TREND
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.Room
import com.personal.homestayfinder.data.models.SearchFilter
import com.personal.homestayfinder.data.models.SearchTrend
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
    @Named(SEARCH_TREND)
    private val searchTrend : CollectionReference,
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
    suspend fun getSearchTrends(cityId: Int) = flow{
        Log.d("cityId","$cityId")
        val result = searchTrend.whereEqualTo("cityId", cityId)
            .orderBy("numOfSearches", Query.Direction.DESCENDING)
           .limit(6).get().await().toObjects(SearchTrend::class.java)
        emit(result)
    }.flowOn(dispatcher)
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
    suspend fun getRoomsByCityId(cityId : Int) = flow{
        val result = room.whereEqualTo("city.id", cityId).get().await().toObjects(Room::class.java).map { it.toRoomListItem() }
        emit(result)
    }
    suspend fun searchRoom(searchAddress : String,searchFilter: SearchFilter?, cityId : Int) = flow{
        val result : MutableList<Room> = arrayListOf()
        val queryResult = room.whereEqualTo("city.id", cityId).get().await().toObjects(Room::class.java)
        val addressParts = searchAddress.split(",")
        val district = addressParts.getOrNull(0)?.trim()?.uppercase()
        val ward = addressParts.getOrNull(1)?.trim()?.uppercase()
        val streetNames = addressParts.getOrNull(2)?.trim()?.uppercase()
        queryResult.forEach outer@ {room ->
            if(district != null){
                if(room.district?.name?.uppercase()?.contains(district) == false) return@outer
            }
            if(ward != null){

                if(room.ward?.name?.uppercase()?.contains(ward) == false) return@outer
            }
            if(streetNames != null){
                if(room.streetNames?.uppercase()?.contains(streetNames) == false) return@outer
            }
            searchFilter?.let {filter ->
                //search rentalPrice
                if(room.rentalPrice!! < filter.minPrice || room.rentalPrice!! > filter.maxPrice) return@outer
                //search RoomType
                if(filter.roomType != null){
                    if(room.roomType?.id != filter.roomType!!.id) return@outer
                }
                //search Capacity
                if(room.capacity!! < filter.capacity!!) return@outer
                //search Gender
                if(filter.gender != ALL_GENDER && room.gender!! != filter.gender && room.gender != ALL_GENDER) return@outer
                //search Utilities
                val tempList = room.utilitiesList
               filter.utilitiesList.forEach{
                    val matchingUtility = tempList.find { utility -> utility.id == it.id} ?: return@outer
                    tempList.remove(matchingUtility)
                }
            }
            result.add(room)
        }
        emit(result.map { it.toRoomListItem() })
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