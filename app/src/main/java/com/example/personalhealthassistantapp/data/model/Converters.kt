package com.example.personalhealthassistantapp.data.model
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        return data?.split(",")?.map { it.trim() }
    }
}
