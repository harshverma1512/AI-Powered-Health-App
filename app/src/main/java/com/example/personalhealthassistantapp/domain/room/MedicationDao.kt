package com.example.personalhealthassistantapp.domain.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.personalhealthassistantapp.data.model.Medication

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMedication(medication: Medication)
    @Query("SELECT * FROM medication")
    suspend fun getAllMedication(): List<Medication>
}