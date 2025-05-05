package com.example.personalhealthassistantapp.domain.services

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.activity.FullScreenAlarmActivity
import com.example.personalhealthassistantapp.utility.SharedPrefManager.Companion.HYDRATION_SCHEDULE
import com.example.personalhealthassistantapp.utility.SharedPrefManager.Companion.SLEEP_SCHEDULE


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var ringtone: Ringtone? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getStringExtra("type")?.equals(SLEEP_SCHEDULE) == true){
            sleepAlarm(context, intent)
            Log.d("AlarmReceiver", "Sleep Reminder")
        }else if (intent.getStringExtra("type")?.equals(HYDRATION_SCHEDULE) == true){
            hydrationAlarm(context)
            Log.d("AlarmReceiver", "Hydration Reminder")
        }
    }


    private fun sleepAlarm(context : Context, intent: Intent){
        val label = intent.getStringExtra("EXTRA_MESSAGE") ?: "Alarm"
        val time = intent.getStringExtra("EXTRA_TIME") ?: ""
        val fullScreenIntent = Intent(context, FullScreenAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_MESSAGE", label)
            putExtra("EXTRA_TIME", time)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Alarm Notifications"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.health_plus) // your small icon
            .setContentTitle("Alarm Ringing!")
            .setContentText(label)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)

        // Start the alarm sound
        ContextCompat.startForegroundService(
            context,
            Intent(context, AlarmSoundService::class.java)
        )
    }

    private fun hydrationAlarm(context: Context){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Hydration Reminder"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Health Reminder")
            .setContentText("Time to check your health or drink water!")
            .setSmallIcon(R.drawable.water_bottle)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

    }

}
