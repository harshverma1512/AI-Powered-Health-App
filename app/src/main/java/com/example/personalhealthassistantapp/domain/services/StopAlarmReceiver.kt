package com.example.personalhealthassistantapp.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("StopAlarmReceiver", "Stopping alarm")
        AlarmReceiver.ringtone?.stop()
    }
}
