package com.example.personalhealthassistantapp.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthassistantapp.data.model.MessageModel
import com.example.personalhealthassistantapp.utility.Utils
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    var score by mutableStateOf<Int?>(null)
        private set

    init {
        // Simulate API call on first launch
        viewModelScope.launch {
            val bmi = calculateBMI(70.0, "kg", 170.0)
          //  sendMsg("based on my on my BMI what's the score of my health? out of 100 my bmi is ${bmi} only tell us in number and nothing else")
            score = bmi.toInt()
        }
    }

    val messageList = mutableStateListOf<MessageModel>()

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro", // Change to a supported model
        apiKey = Utils.GEMINI_KEY
    )

    private fun Double.roundTo(decimals: Int): Double {
        return "%.${decimals}f".format(this).toDouble()
    }


    private fun calculateBMI(weight: Double, weightUnit: String, heightCm: Double): Double {
        // Convert weight to kg if it's in lbs
        val weightInKg = if (weightUnit.lowercase() == "lbs") {
            weight * 0.453592
        } else {
            weight
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
                messageList.add(MessageModel("Error: Unable to fetch response Support Model Error", false))
            }
        }
    }

}