package com.example.personalhealthassistantapp.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthassistantapp.data.model.MessageModel
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import com.example.personalhealthassistantapp.utility.Utils
import com.example.personalhealthassistantapp.utility.Utils.fetchCurrentUserData
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    var score by mutableStateOf<Int?>(null)
    var userData by mutableStateOf<Map<String, Any>?>(null)

    init {
        // Simulate API call on first launch
        fetchCurrentUserData(onResult = {
            userData = it
            Log.d("checkUserData", it.toString())
            userData?.let {
                viewModelScope.launch {
                    val bmi = calculateBMI(userData?.get(SharedPrefManager.WEIGHT) as Long,
                        userData?.get(SharedPrefManager.WEIGHT_MEASUREMENT) as String,
                        userData?.get(SharedPrefManager.HEIGHT) as Long)
                    score = bmi.toInt()
                }
            }
        }, onError = {
            userData = null
        })
    }

    val messageList = mutableStateListOf<MessageModel>()

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro", // Change to a supported model
        apiKey = Utils.GEMINI_KEY
    )

    private fun Double.roundTo(decimals: Int): Double {
        return "%.${decimals}f".format(this).toDouble()
    }


    private fun calculateBMI(weight: Long, weightUnit: String, heightCm: Long): Double {
        // Convert weight to kg if it's in lbs
        val weightInKg = if (weightUnit.lowercase() == "lbs") {
            weight.toDouble() * 0.453592
        } else {
            weight.toDouble()
        }

        // Convert height from cm to meters
        val heightInMeters = heightCm / 100

        // BMI formula: weight (kg) / height² (m²)
        return (weightInKg / (heightInMeters * heightInMeters)).roundTo(2)
    }

    fun sendMsg(question: String) {
        viewModelScope.launch {
            try {
                val model = generativeModel
                if (model == null) {
                    Log.e("ChatViewModel", "GenerativeModel is null. Check API Key.")
                    messageList.add(MessageModel("Error: API Key missing or invalid", false))
                    return@launch
                }

                val chat = model.startChat()
                messageList.add(MessageModel(question, true))
                messageList.add(MessageModel("Typing...", false))

                val response = chat.sendMessage(question)
                Log.d("ChatViewModel", response.text.toString())
                if (Build.VERSION.SDK_INT >= 35) {
                    messageList.removeLast()
                } else {
                    messageList.removeAt(messageList.size - 1)
                }

                messageList.add(MessageModel(response.text.toString(), false))

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message: ${e.message}")
                if (Build.VERSION.SDK_INT >= 35) {
                    messageList.removeLast()
                } else {
                    messageList.removeAt(messageList.size - 1)
                }
                messageList.add(MessageModel("Error: Unable to fetch response  ${e.message}", false))
            }
        }
    }

}