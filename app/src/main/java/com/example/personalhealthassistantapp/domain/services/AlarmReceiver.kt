package com.example.personalhealthassistantapp.domain.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.data.model.AlarmModel
import com.example.personalhealthassistantapp.presentation.activity.MainActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var ringtone: Ringtone? = null
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        val label = intent.getStringExtra("EXTRA_MESSAGE") ?: return
        val time = intent.getStringExtra("EXTRA_TIME") ?: return

        // Set alarm volume to max
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
            0
        )

        // Play alarm sound
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val actualUri = alarmUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ringtone = RingtoneManager.getRingtone(context, actualUri)
        ringtone?.play()

       showNotificationWithStopAction(context, label, time)
    }

    private fun showNotificationWithStopAction(context: Context, message: String, time: String) {
        val channelId = "alarm_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        val channel = NotificationChannel(
            channelId,
            "Alarm Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        // Stop alarm action
        val stopIntent = Intent(context, StopAlarmReceiver::class.java).apply {
            putExtra("EXTRA_DATA", message)
            putExtra("EXTRA_TIME", time)
        }

        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        ).apply {
            notificationManager.cancel(0)
        }

        // Full-screen intent to wake up the screen
        val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "alarm") // optional: routing logic if needed
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Notification intent
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "snooze")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        ).apply {
            notificationManager.cancel(0)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Health Assistant Alarm")
            .setContentText(message)
            .setSmallIcon(R.drawable.health_plus)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true) // Wake up the screen
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.stop_notification, "Stop Alarm", stopPendingIntent)
            .setOngoing(true) // Prevent swipe to dismiss
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }
}
