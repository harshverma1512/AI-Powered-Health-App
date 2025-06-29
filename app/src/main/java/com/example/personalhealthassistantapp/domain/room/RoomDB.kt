package com.example.personalhealthassistantapp.domain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.personalhealthassistantapp.data.model.Converters
import com.example.personalhealthassistantapp.data.model.Medication
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [SleepHistoryModel::class, Medication::class], version = 1, exportSchema = false )
@TypeConverters(Converters::class)
abstract class RoomDB : RoomDatabase(){
    abstract fun sleepDao(): SleepHistoryDao
    abstract fun medicationDao(): MedicationDao
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoomDB {
            return Room.databaseBuilder(
                context,
                RoomDB::class.java,
                "sleep_history_database"
            ).build()
    }

    @Provides
    fun provideSleepDao(database: RoomDB): SleepHistoryDao = database.sleepDao()

    @Provides
    fun provideMedicationDao(database: RoomDB): MedicationDao = database.medicationDao()
}
