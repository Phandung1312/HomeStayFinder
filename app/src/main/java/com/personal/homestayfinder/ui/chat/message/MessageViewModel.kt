package com.personal.homestayfinder.ui.chat.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.base.viewmodel.ImageViewModel
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.data.repositories.ChatRepository
import com.personal.homestayfinder.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatRepository: ChatRepository,

) : ImageViewModel() {
    private  var currentMessage = MutableLiveData(Message())
    var receiver = MutableLiveData<User>()
    private set


    fun getReceiverInfo(userId : String){
        showSmallLoading(true)
        parentJob = viewModelScope.launch(handler) {
            chatRepository.getReceiverInfo(userId).collect{
                receiver.postValue(it)
                showSmallLoading(false)

            }
        }
        registerJobFinish()
    }
    fun sendMessage(senderId : String) {
        viewModelScope.launch(handler) {
            currentMessage.value?.let {message ->
                message.sender = senderId
                message.receiver = receiver.value?.userId
                message.imagesUrl = imagesList.value?.map { it.toString() }?.toMutableList() ?: ArrayList()
                setDateTimeSubmit()
            }
            chatRepository.sendMessage(currentMessage.value!!)
            imagesList.value?.clear()
            currentMessage.postValue(Message())
        }
    }
    private fun setDateTimeSubmit(){
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy")
        currentMessage.value?.time = dateFormat.format(calendar.time)
    }
    fun seenMessage(recipientId: String, currentUserId: String){
        viewModelScope.launch(handler) {
            chatRepository.seenMessage(recipientId, currentUserId)
        }
    }
    fun getReferentCurrentUserChat(currentUserId : String, recipientId : String) : DatabaseReference{
        return chatRepository.getReferentCurrentUserChat(currentUserId, recipientId)
    }

    fun onContentChanged(text: CharSequence?,
                       start: Int,
                       before: Int,
                       count: Int){
        text?.let{
            if(it.isNotBlank()) currentMessage.value?.content = it.toString()
        }
    }
}