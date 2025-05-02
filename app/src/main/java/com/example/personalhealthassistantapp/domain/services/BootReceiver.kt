package com.example.personalhealthassistantapp.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import java.time.LocalTime


class BootReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = SharedPrefManager(context)

            val sleepTime = LocalTime.of(prefs.getSleepHour(), prefs.getSleepMinute())
            val wakeTime = LocalTime.of(prefs.getWakeHour(), prefs.getWakeMinute())

                val selectedDays = prefs.getSelectedDays() // Fetch selected days from SharedPrefs
      //      scheduleDailyAlarms(context, sleepTime, wakeTime, selectedDays)
        }
    }
}
