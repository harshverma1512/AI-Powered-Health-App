package com.example.personalhealthassistantapp.domain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [SleepHistoryModel::class], version = 1, exportSchema = false )
abstract class RoomDB : RoomDatabase(){
    abstract fun sleepDao(): SleepHistoryDao
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
}
