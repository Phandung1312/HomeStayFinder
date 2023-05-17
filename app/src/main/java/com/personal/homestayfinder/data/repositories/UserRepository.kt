package com.personal.homestayfinder.data.repositories

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.personal.homestayfinder.common.Constant

import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserRepository @Inject constructor(
    @Named(Constant.USER)
    private val user : CollectionReference,
    @IoDispatcher
    private val dispatcher : CoroutineDispatcher) {
    suspend fun updateUser(newUser : User) : Unit = withContext(dispatcher){
        user.document(newUser.userId!!).set(newUser).await()
    }
    suspend fun changeStatus(idUser : String, online : Boolean) : Unit = withContext(dispatcher){
        user.document(idUser).update("online",online).await()
    }
    suspend fun getUserById(idUser : String) = flow{
            val result = user.document(idUser).get().await().toObject(User::class.java)
            emit(result!!)
    }.flowOn(dispatcher)

}