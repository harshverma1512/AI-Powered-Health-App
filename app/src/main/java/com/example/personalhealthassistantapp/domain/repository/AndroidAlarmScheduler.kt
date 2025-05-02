package com.example.personalhealthassistantapp.domain.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.personalhealthassistantapp.data.interfaces.AlarmScheduler
import com.example.personalhealthassistantapp.data.model.AlarmModel
import com.example.personalhealthassistantapp.domain.services.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(@ApplicationContext private val context: Context) :
    AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(time: AlarmModel) {

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("EXTRA_MESSAGE", time.message)
                putExtra("EXTRA_TIME", time.time.toString())
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                time.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerTimeMillis = time.time
                .atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
    }

    override fun cancel(time: AlarmModel) {
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            time.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}