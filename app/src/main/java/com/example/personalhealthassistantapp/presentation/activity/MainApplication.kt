package com.example.personalhealthassistantapp.presentation.activity

import android.app.Application
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDate

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        isHydrationDateChanged()
        FirebaseApp.initializeApp(this)

    }

    private fun isHydrationDateChanged(){
        if (SharedPrefManager(this).getToday() != LocalDate.now().toString()){
            SharedPrefManager(this).saveTodayDate(LocalDate.now())
            SharedPrefManager(this).clearHydrationData()
        }
    }
}