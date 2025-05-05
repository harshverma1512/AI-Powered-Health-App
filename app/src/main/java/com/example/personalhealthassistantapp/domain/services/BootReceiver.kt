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
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {

            val sharedPrefManager = SharedPrefManager(context)
            val alarmScheduler = AndroidAlarmScheduler(context)
            val sleepDate = sharedPrefManager.getSleepDate()
            val sleepTime = sharedPrefManager.getSleepTime()
            val wakeUpTime = sharedPrefManager.getWakeupTime()
            Log.d("BootReceiver", "Sleep time: $sleepDate $sleepTime")
            if (sleepDate?.isNotEmpty() == true && sleepTime?.isNotEmpty() == true) {
                val sleepDateTime =
                    LocalDateTime.of(LocalDate.parse(sleepDate), LocalTime.parse(sleepTime))
                val wakeUpDateTime =
                    LocalDateTime.of(LocalDate.parse(sleepDate), LocalTime.parse(wakeUpTime))
                alarmScheduler.schedule(AlarmModel(sleepDateTime, "Sleep Time"))
                alarmScheduler.schedule(AlarmModel(wakeUpDateTime, "Wakeup Time"))
            }
            if (SharedPrefManager(context).getHydrationNotify()){
                alarmScheduler.hydrationSchedule(3)
            }
        }
    }
}
