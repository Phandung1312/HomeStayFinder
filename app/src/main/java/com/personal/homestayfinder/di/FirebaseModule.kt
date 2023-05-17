package com.personal.homestayfinder.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.personal.homestayfinder.common.Constant.CHAT
import com.personal.homestayfinder.common.Constant.FAVORITE
import com.personal.homestayfinder.common.Constant.MESSAGE_IMAGE
import com.personal.homestayfinder.common.Constant.ROOM
import com.personal.homestayfinder.common.Constant.ROOM_IMAGE
import com.personal.homestayfinder.common.Constant.USER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = Firebase.auth
    @Provides
    @Singleton
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Named(USER)
    fun providesUser(db : FirebaseFirestore) = db.collection(USER)

    @Provides
    @Named(ROOM)
    fun providesRoom(db : FirebaseFirestore) = db.collection(ROOM)

    @Provides
    @Named(CHAT)
    fun providesChats(db : FirebaseDatabase) = db.reference.child(CHAT)

    @Provides
    @Named(ROOM_IMAGE)
    fun providesRoomImages(db : FirebaseStorage) = db.reference.child(ROOM_IMAGE)

    @Provides
    @Named(MESSAGE_IMAGE)
    fun providesMessageImages(db : FirebaseStorage) = db.reference.child(MESSAGE_IMAGE)
    @Provides
    @Named(FAVORITE)
    fun providesFavorite(db : FirebaseFirestore) = db.collection(FAVORITE)
}