package com.example.personalhealthassistantapp.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.personalhealthassistantapp.data.model.AlarmModel
import com.example.personalhealthassistantapp.domain.repository.AndroidAlarmScheduler
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import java.time.LocalDateTime

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("StopAlarmReceiver", "Stopping alarm")
        AlarmReceiver.ringtone?.stop()
        val alarmScheduler = AndroidAlarmScheduler(context)
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val time = intent?.getStringExtra("EXTRA_TIME") ?: return
        alarmScheduler.cancel(AlarmModel(time as LocalDateTime, message))
    }
}
