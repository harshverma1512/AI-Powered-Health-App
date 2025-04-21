package com.example.personalhealthassistantapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_history")
data class SleepHistoryModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sleepType: String?,
    val sleepDuration: Int?,
    val sleepDate: String?,
)