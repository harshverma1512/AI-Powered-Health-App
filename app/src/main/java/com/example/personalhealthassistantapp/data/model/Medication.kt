package com.example.personalhealthassistantapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity("medication")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val name: String?= "",
    val instructions: String?= "",
    val dosage: String?= "",
    val startDate: String?= "",
    val endDate: String?= "",
    val mealTiming: String?= "",
    val checked: Boolean?= false,
    val time: String?= null
)

