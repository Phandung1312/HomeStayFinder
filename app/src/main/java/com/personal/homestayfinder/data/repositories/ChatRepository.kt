package com.personal.homestayfinder.data.repositories

import androidx.core.net.toUri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.personal.homestayfinder.common.Constant.CHAT
import com.personal.homestayfinder.common.Constant.MESSAGE_IMAGE
import com.personal.homestayfinder.common.Constant.USER
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.di.IoDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class ChatRepository @Inject constructor(
    @Named(MESSAGE_IMAGE)
    private val messageImages : StorageReference,
    @Named(CHAT)
    private val chat: DatabaseReference,
    @Named(USER)
    private val user: CollectionReference,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getListIdUsers(queryId: String) = withContext(dispatcher) {
        chat.child(queryId)
            .get()
            .await()
            .children
            .mapNotNull { it?.key }
            .toList()
    }

    suspend fun getListUserChats(idUsers: List<String>): Flow<List<User>> = callbackFlow {
        val query = user.whereIn("userId", idUsers)
        val registration = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val users = snapshot.documents.mapNotNull { document ->
                    document.toObject(User::class.java)
                }
                this.trySend(users).isSuccess
            }
            else{
                this.trySend(emptyList<User>()).isSuccess
            }
        }
        awaitClose { registration.remove() }
    }.flowOn(dispatcher)
    suspend fun sendMessage(message : Message) = withContext(dispatcher) {
        message.imagesUrl?.let {
            val urlsDeferred: List<Deferred<String>> = it.map { uri ->
                async {
                    val fileName = UUID.randomUUID().toString()
                    val imageRef = messageImages.child(message.sender!!).child(message.receiver!!)
                        .child(fileName)
                    imageRef.putFile(uri.toUri()).await()
                    val url = imageRef.downloadUrl.await()
                    url.toString()
                }
            }
            message.imagesUrl = urlsDeferred.awaitAll().toMutableList()
        }
        chat.child(message.sender!!).child(message.receiver!!).push().setValue(message)
        chat.child(message.receiver!!).child(message.sender!!).push().setValue(message)
    }
    suspend fun seenMessage(recipientId : String, currentUserId : String) = withContext(dispatcher){
        val databaseReference = chat.child("$recipientId/$currentUserId").limitToLast(10)
        val messagesSnapshot = databaseReference.get().await()
        val hashMap = HashMap<String, Any>()
        hashMap["seen"] = true
        messagesSnapshot.children.forEach{ ds ->
            val message = ds.getValue(Message::class.java)
            if(message != null){
                ds.ref.updateChildren(hashMap)
            }
        }
    }
    suspend fun getLastMessage(ownerId : String, participantId : String) : Flow<Message> = callbackFlow<Message> {
        val query = chat.child("$ownerId/$participantId")
            .orderByKey().limitToLast(1)
        val registration = query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val message = ds.getValue(Message::class.java)
                        message?.let {
                            trySend(it).isSuccess
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { query.removeEventListener(registration) }
    }.flowOn(dispatcher)
    fun getReceiverInfo(idUser: String): Flow<User> = callbackFlow {
        val listener = user.document(idUser).addSnapshotListener { snapshot, error ->
            if (error != null) {
                cancel(CancellationException("Error getting user with ID $idUser", error))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    this.trySend(user).isSuccess
                }
            }
        }
        awaitClose { listener.remove() }
    }.flowOn(dispatcher)

    fun getReferentCurrentUserChat(currentUserId : String, recipientId : String) : DatabaseReference{
        return chat.child(currentUserId).child(recipientId)
    }
}