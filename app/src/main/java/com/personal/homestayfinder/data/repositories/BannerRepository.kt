package com.personal.homestayfinder.data.repositories


import com.google.firebase.firestore.CollectionReference
import com.personal.homestayfinder.common.Constant.BANNER
import com.personal.homestayfinder.data.models.Banner
import com.personal.homestayfinder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class BannerRepository @Inject constructor(
    @Named(BANNER)
    private val banner : CollectionReference,
    @IoDispatcher
    private val dispatcher : CoroutineDispatcher
) {

    suspend fun getAllBanners() = flow{
        val result = banner.get().await().toObjects(Banner::class.java).map{ it.toSlideModel()}
        emit(result)
    }.flowOn(dispatcher)
}