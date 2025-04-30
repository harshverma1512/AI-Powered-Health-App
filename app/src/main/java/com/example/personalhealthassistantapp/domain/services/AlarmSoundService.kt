package com.example.personalhealthassistantapp.domain.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.personalhealthassistantapp.R

class AlarmSoundService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        playAlarmSound()
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "alarm_sound_channel"
        val channel = NotificationChannel(
            channelId,
            "Personal Health Alarm Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Alarm Ringing...")
            .setContentText("Swipe to stop the alarm.")
            .setSmallIcon(R.drawable.health_plus)
            .build()
    }

    private fun playAlarmSound() {
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0)

        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_ALARM)
            setDataSource(this@AlarmSoundService, alarmUri)
            isLooping = true
            prepare()
            start()
        }
    }


    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
