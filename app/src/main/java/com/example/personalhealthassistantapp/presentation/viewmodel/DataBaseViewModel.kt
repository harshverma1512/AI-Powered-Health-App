package com.example.personalhealthassistantapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import com.example.personalhealthassistantapp.domain.repository.SleepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataBaseViewModel @Inject constructor(private val sleepRepository: SleepRepository) : ViewModel() {

    suspend fun insertSleepHistory(sleepHistory: SleepHistoryModel) {
        sleepRepository.insertSleepHistory(sleepHistory)
    }

    suspend fun getAllSleepHistory(): List<SleepHistoryModel> {
        return sleepRepository.getAllSleepHistory()
    }

}