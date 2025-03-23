package com.example.personalhealthassistantapp.presentation.activity

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.koin.core.context.startKoin

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {


        }
    }
}