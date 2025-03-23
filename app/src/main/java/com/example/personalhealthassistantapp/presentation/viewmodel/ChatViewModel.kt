package com.example.personalhealthassistantapp.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthassistantapp.data.model.MessageModel
import com.example.personalhealthassistantapp.Utils
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro", // Change to a supported model
        apiKey = Utils.GEMINI_KEY
    )


    fun sendMsg(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat()
                messageList.add(MessageModel(question, true))
                messageList.add(MessageModel("Typing...", false))

                val response = chat.sendMessage(question)
                Log.d("ChatViewModel", response.text.toString())
                if (Build.VERSION.SDK_INT >= 35) {
                    messageList.removeLast()
                }else{
                    messageList.removeAt(messageList.size - 1)
                }
                messageList.add(MessageModel(response.text.toString(), false))
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message: ${e.message}")
                messageList.add(MessageModel("Error: Unable to fetch response", false))
            }
        }
    }

}