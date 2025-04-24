package com.example.personalhealthassistantapp.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AlarmModel(@Serializable val time: LocalDateTime, @Serializable val message: String)