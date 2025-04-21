package com.example.personalhealthassistantapp.domain.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel

@Dao
interface SleepHistoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSleepHistory(sleepHistory: SleepHistoryModel)
    @Query("SELECT * FROM sleep_history")
    suspend fun getAllSleepHistory(): List<SleepHistoryModel>

}