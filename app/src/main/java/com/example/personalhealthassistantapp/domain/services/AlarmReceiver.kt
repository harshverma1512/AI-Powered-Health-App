package com.example.personalhealthassistantapp.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent) {
        val label = intent.getStringExtra("label") ?: "Alarm"
        Toast.makeText(context, "$label is ringing!", Toast.LENGTH_LONG).show()
    }
}