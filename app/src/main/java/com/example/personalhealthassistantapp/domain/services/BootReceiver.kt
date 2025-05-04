package com.example.personalhealthassistantapp.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.personalhealthassistantapp.data.model.AlarmModel
import com.example.personalhealthassistantapp.domain.repository.AndroidAlarmScheduler
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val sharedPrefManager = SharedPrefManager(context)
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val alarmScheduler = AndroidAlarmScheduler(context)
            Log.d("call", "yes its calling")
//            alarmScheduler.schedule(AlarmModel(LocalDateTime.of(LocalDate.parse(sharedPrefManager.getSleepDate()),LocalTime.parse(sharedPrefManager.getSleepTime())), ""))
//            alarmScheduler.schedule(AlarmModel(LocalDateTime.of(LocalDate.parse(sharedPrefManager.getSleepDate()),LocalTime.parse(sharedPrefManager.getWakeupTime())), ""))
//            alarmScheduler.hydrationSchedule(3)
        }
    }
}
