package com.example.personalhealthassistantapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.domain.repository.AndroidAlarmScheduler
import com.example.personalhealthassistantapp.utility.SharedPrefManager

@Composable
fun NotificationSetupScreen(navController: NavController) {
    val context = LocalContext.current
    var healthInsights by remember { mutableStateOf(SharedPrefManager(context).getHealthNotify()) }
    var medicationReminder by remember { mutableStateOf(SharedPrefManager(context).getHealthAssistantNotify()) }
    val alarmScheduler = AndroidAlarmScheduler(context)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colorResource(R.color.backgroundColor))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            // Illustration (Placeholder)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
            ) {
                Image(
                    painterResource(R.drawable.stop_notification),
                    contentDescription = "Notification Setting",
                    Modifier.background(
                        colorResource(R.color.backgroundColor)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Notification Setup", fontSize = 32.sp, fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notification Toggles
            NotificationToggle("Hydration Reminder", R.drawable.drop, healthInsights) {
                if (it) {
                    Toast.makeText(
                        context,
                        "We will notify you every 3 hours to drink water.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                alarmScheduler.hydrationSchedule(3)
                healthInsights = it
                SharedPrefManager(context).saveHealthNotify(it)

            }
            NotificationToggle(
                "Medication Reminder", R.drawable.stop_notification, medicationReminder
            ) {
                SharedPrefManager(context).saveHealthAssistantNotify(it)
                medicationReminder = it
            }
            NotificationToggle("Sleep Alarm Reminder", R.drawable.sleep, true) {
                Toast.makeText(
                    context,
                    "Sleep reminder wasn't turned off to maintain alarm functionality.",
                    Toast.LENGTH_LONG
                ).show()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Allow All Button
            Button(
                onClick = {
                    healthInsights = true
                    medicationReminder = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF406AFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Allow All", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.Check, contentDescription = "Check")
            }
        }
    }
}

@Composable
fun NotificationToggle(
    title: String, icon: Int, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF406AFF),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Preview
@Composable
fun PreviewNotificationSetupScreen() {
    NotificationSetupScreen(navController = NavController(LocalContext.current))
}
