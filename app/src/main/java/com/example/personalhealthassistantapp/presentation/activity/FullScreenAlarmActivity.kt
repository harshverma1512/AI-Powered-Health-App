package com.example.personalhealthassistantapp.presentation.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.domain.services.AlarmSoundService
import com.example.personalhealthassistantapp.presentation.ui.PersonalHealthAssistantAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenAlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val label = intent?.getStringExtra("EXTRA_MESSAGE")
        // Set full-screen flags if needed (optional)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN

        setContent {
            PersonalHealthAssistantAppTheme {
                    AlarmScreen(label ?: "",
                        onStopAlarm = {
                            stopService(Intent(this, AlarmSoundService::class.java))
                            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.cancel(1001)
                            finish()
                        }
                    )
            }
        }
    }
}

@Composable
fun AlarmScreen(label : String?, onStopAlarm: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "⏰  ${label ?: "Wake Up!"}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStopAlarm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Stop Alarm", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}
