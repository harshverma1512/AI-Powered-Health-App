package com.example.personalhealthassistantapp.domain.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.personalhealthassistantapp.R
import java.time.LocalTime

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        if (!isReminderEnabled(applicationContext)) {
            return Result.success() // Don't do anything if disabled
        }

        val now = LocalTime.now()
        val start = LocalTime.of(7, 0) // 7 AM
        val end = LocalTime.of(22, 0) // 10 PM

        if (now.isBefore(start) || now.isAfter(end)) {
            return Result.success() // Don't notify outside time window
        }

        showNotification()
        return Result.success()
    }

    private fun isReminderEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getBoolean("sleep_reminder_enabled", false)
    }

    private fun showNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "sleep_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sleep Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Hydration reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Hydrate Time...")
            .setContentText("Hydration time take care of your health.")
            .setSmallIcon(R.drawable.water_bottle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
