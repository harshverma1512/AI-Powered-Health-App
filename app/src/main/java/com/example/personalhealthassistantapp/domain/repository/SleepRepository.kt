package com.example.personalhealthassistantapp.domain.repository

import android.content.Context
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import com.example.personalhealthassistantapp.domain.room.DatabaseModule.provideDatabase
import com.example.personalhealthassistantapp.domain.room.SleepHistoryDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SleepRepository @Inject constructor(@ApplicationContext private val context: Context) : SleepHistoryDao {
    override suspend fun insertSleepHistory(sleepHistory: SleepHistoryModel) {
        provideDatabase(context).sleepDao().insertSleepHistory(sleepHistory)
    }

    override suspend fun getAllSleepHistory(): List<SleepHistoryModel> {
       return provideDatabase(context).sleepDao().getAllSleepHistory()
    }
}