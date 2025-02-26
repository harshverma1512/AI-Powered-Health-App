package com.example.personalhealthassistantapp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthassistantapp.Utils.GEMINI_KEY
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
        modelName = "gemini-pro", // Change to a supported model
        apiKey = GEMINI_KEY
    )


    fun sendMsg(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat()
                messageList.add(MessageModel(question, true))

                val response = chat.sendMessage(question)
                Log.d("ChatViewModel", response.text.toString())
                messageList.add(MessageModel(response.text.toString(), false))
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message: ${e.message}")
                messageList.add(MessageModel("Error: Unable to fetch response", false))
            }
        }
    }

}