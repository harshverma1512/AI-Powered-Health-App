package com.example.personalhealthassistantapp.domain.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.activity.MainActivity


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var ringtone: Ringtone? = null
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        val label = intent.getStringExtra("EXTRA_MESSAGE") ?: return
        Log.d("AlarmReceiver", "Received alarm for label: $label")

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()

        showNotificationWithStopAction(context, label)
    }

    private fun showNotificationWithStopAction(context: Context, message: String) {
        val channelId = "alarm_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Alarm Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val stopIntent = Intent(context, StopAlarmReceiver::class.java)

        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "snooze")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        ).apply{
            notificationManager.cancel(0)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Health Assistant Alarm")
            .setContentText(message)
            .setSmallIcon(R.drawable.health_plus)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.stop_notification, "Stop Alarm", stopPendingIntent)
            .setOngoing(true) // prevent swipe to dismiss
            .build()

        notificationManager.notify(0, notification)
    }

}