package com.personal.homestayfinder.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.personal.homestayfinder.common.Event
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {

    var firebaseNetworkException = MutableLiveData<Event<FirebaseNetworkException>>()
        protected set

    var firebaseAuthException = MutableLiveData<Event<FirebaseAuthException>>()
        protected set
    var unknownExceptionString = MutableLiveData<Event<String>>()
        protected set
    var isFragmentLoading = MutableLiveData<Event<Boolean>>()
        protected set
    var isScreenLoading = MutableLiveData<Event<Boolean>>()
        protected set
    var isSmallLoading = MutableLiveData<Event<Boolean>>()
        protected set

    var onNavigateToPage = MutableLiveData<Event<Int>>()
        protected set

    var errorMessageResourceId = MutableLiveData<Event<Int>>()
        protected set

    var notifyMessageResourceId = MutableLiveData<Event<Int>>()
        protected set

    var isLoadingMore = MutableLiveData<Event<Boolean>>()
        protected set

    var parentJob: Job? = null
        protected set

    protected fun registerJobFinish() {
        parentJob?.invokeOnCompletion {
            showSmallLoading(false)
            showScreenLoading(false)
        }
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        parseErrorCallFirebase(exception)
    }

    protected fun showError(messageId: Int) {
        errorMessageResourceId.postValue(Event(messageId))
    }

    protected fun showNotify(messageId: Int) {
        notifyMessageResourceId.postValue(Event(messageId))
    }

    protected fun addFirebaseNetworkException(exception: FirebaseNetworkException) {
        firebaseNetworkException.postValue(Event(exception))
    }

    protected fun addFirebaseAuthException(exception: FirebaseAuthException) {
        firebaseAuthException.postValue(Event(exception))
    }
    protected fun showFragmentLoading(isShow: Boolean){
        isFragmentLoading.postValue(Event(isShow))
    }
    protected fun showSmallLoading(isShow: Boolean) {
        isSmallLoading.postValue(Event(isShow))
    }

    protected fun showScreenLoading(isShow: Boolean) {
        isScreenLoading.postValue(Event(isShow))
    }

    protected fun showLoadingMore(isShow: Boolean) {
        isLoadingMore.postValue(Event(isShow))
    }

    protected fun navigateToPage(actionId: Int) {
        onNavigateToPage.postValue(Event(actionId))
    }

    protected open fun parseErrorCallFirebase(e: Throwable) {
        when (e) {
            is FirebaseNetworkException -> {
                firebaseNetworkException.postValue(Event(e))
            }

            is FirebaseAuthException -> {
                firebaseAuthException.postValue(Event(e))
            }

            else -> {
                val errorString = "Đã xảy ra lỗi không mong muốn."
                unknownExceptionString.postValue(Event(errorString))
            }
        }
    }

    open fun fetchData() {

    }

}