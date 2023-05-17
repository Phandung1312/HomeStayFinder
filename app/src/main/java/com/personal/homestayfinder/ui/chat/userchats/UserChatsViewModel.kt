package com.personal.homestayfinder.ui.chat.userchats


import androidx.lifecycle.liveData
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.data.repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserChatsViewModel @Inject constructor(private val chatRepository: ChatRepository) :
    BaseViewModel() {
    fun getListIdUsers(queryId: String) = liveData(handler){
        showSmallLoading(true)
        try{
            emit(chatRepository.getListIdUsers(queryId))
        } finally {
            showSmallLoading(false)
        }
    }

    fun getListUserChats(idUsers: List<String>) = liveData(handler){
        showSmallLoading(true)
        try {
            chatRepository.getListUserChats(idUsers).collect { users ->
                emit(users)
                showSmallLoading(false)
            }
        } finally {
            showSmallLoading(false)
        }
    }

    fun getLastMessage(ownerId: String, participantId: String) = liveData(handler){
            chatRepository.getLastMessage(ownerId, participantId)
                .collect{ message ->
                    emit(message)
                }
    }
}