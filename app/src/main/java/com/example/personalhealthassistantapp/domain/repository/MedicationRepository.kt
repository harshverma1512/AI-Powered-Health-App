package com.example.personalhealthassistantapp.domain.repository

import android.content.Context
import com.example.personalhealthassistantapp.data.model.Medication
import com.example.personalhealthassistantapp.domain.room.DatabaseModule.provideDatabase
import com.example.personalhealthassistantapp.domain.room.MedicationDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MedicationRepository @Inject constructor(@ApplicationContext private val context: Context): MedicationDao {
    override suspend fun insertMedication(medication: Medication) {
        provideDatabase(context).medicationDao().insertMedication(medication)
    }

    override suspend fun getAllMedication(): List<Medication> {
      return  provideDatabase(context).medicationDao().getAllMedication()
    }
}