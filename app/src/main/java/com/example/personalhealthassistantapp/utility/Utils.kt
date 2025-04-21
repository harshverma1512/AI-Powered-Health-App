package com.example.personalhealthassistantapp.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.domain.services.AlarmReceiver
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

object Utils {

    const val GEMINI_KEY = "AIzaSyBdEME3G08fi4tMdMLIasIQjeVuUfv2IDE"

    @RequiresApi(Build.VERSION_CODES.O)
    fun DayOfWeek.toCalendarDay(): Int = when (this) {
        DayOfWeek.SUNDAY -> Calendar.SUNDAY
        DayOfWeek.MONDAY -> Calendar.MONDAY
        DayOfWeek.TUESDAY -> Calendar.TUESDAY
        DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
        DayOfWeek.THURSDAY -> Calendar.THURSDAY
        DayOfWeek.FRIDAY -> Calendar.FRIDAY
        DayOfWeek.SATURDAY -> Calendar.SATURDAY
    }


    fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                if (intent.resolveActivity(context.packageManager) != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleAlarm(
        context: Context,
        time: LocalDateTime,
        daysOfWeek: Set<DayOfWeek>?,
        label: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (daysOfWeek.isNullOrEmpty()) {
            // One-time alarm
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, time.hour)
                set(Calendar.MINUTE, time.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1) // Schedule for next day if time already passed
                }
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("label", label)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                label.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            // Repeating alarms for each selected day
            for (day in daysOfWeek) {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, time.hour)
                    set(Calendar.MINUTE, time.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    val currentDay = get(Calendar.DAY_OF_WEEK)
                    val targetDay = day.toCalendarDay()
                    var daysUntil = (targetDay - currentDay + 7) % 7
                    if (daysUntil == 0 && before(Calendar.getInstance())) {
                        daysUntil = 7 // schedule for next week if time already passed today
                    }
                    add(Calendar.DAY_OF_YEAR, daysUntil)
                }

                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("label", "$label on $day")
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    (label + day.name).hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }


    @Composable
    fun BackBtn(
        onBackClick: () -> Unit = {},
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_icon),
            contentDescription = "",
            modifier = Modifier.clickable {
                onBackClick()
            })
    }
}