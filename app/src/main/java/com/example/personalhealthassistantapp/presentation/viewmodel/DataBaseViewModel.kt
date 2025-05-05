package com.example.personalhealthassistantapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.personalhealthassistantapp.data.model.Medication
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import com.example.personalhealthassistantapp.domain.repository.MedicationRepository
import com.example.personalhealthassistantapp.domain.repository.SleepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataBaseViewModel @Inject constructor(
    private val sleepRepository: SleepRepository,
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    suspend fun insertSleepHistory(sleepHistory: SleepHistoryModel) {
        sleepRepository.insertSleepHistory(sleepHistory)
    }

    suspend fun getAllSleepHistory(): List<SleepHistoryModel> {
        return sleepRepository.getAllSleepHistory()
    }

    suspend fun insertMedication(medication: Medication) {
        medicationRepository.insertMedication(medication)
    }

    suspend fun getAllMedication(): List<Medication> {
        return medicationRepository.getAllMedication()
    }
}