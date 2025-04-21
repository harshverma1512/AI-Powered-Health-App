package com.example.personalhealthassistantapp.data.model

import androidx.room.Entity

@Entity(tableName = "sleep_history")
data class SleepHistoryModel(
    val sleepType: String?,
    val sleepDuration: Int?,
    val sleepStatus: String?,
    val sleepDate: String?
)