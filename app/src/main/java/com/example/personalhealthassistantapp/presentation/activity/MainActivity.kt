package com.example.personalhealthassistantapp.presentation.activity

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personalhealthassistantapp.NotificationSetupScreen
import com.example.personalhealthassistantapp.presentation.ChatBotScreen
import com.example.personalhealthassistantapp.presentation.HealthTextAnalysisScreen
import com.example.personalhealthassistantapp.presentation.HeightPickerScreen
import com.example.personalhealthassistantapp.presentation.HomeScreen
import com.example.personalhealthassistantapp.presentation.HydrationRecord
import com.example.personalhealthassistantapp.presentation.LoginSignupScreen
import com.example.personalhealthassistantapp.presentation.ProfileScreen
import com.example.personalhealthassistantapp.presentation.ScreensName
import com.example.personalhealthassistantapp.presentation.SleepTrackingScreen
import com.example.personalhealthassistantapp.presentation.SplashScreen
import com.example.personalhealthassistantapp.presentation.SymptomsInputScreen
import com.example.personalhealthassistantapp.presentation.WaterGoalSetupScreen
import com.example.personalhealthassistantapp.presentation.WeightPickerScreen
import com.example.personalhealthassistantapp.presentation.ui.PersonalHealthAssistantAppTheme
import com.example.personalhealthassistantapp.presentation.viewmodel.ChatViewModel
import com.example.personalhealthassistantapp.presentation.viewmodel.DataBaseViewModel
import com.example.personalhealthassistantapp.presentation.welcomScreen.WelcomeScreenFirst
import com.example.personalhealthassistantapp.presentation.welcomScreen.WelcomeScreenSecond
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestRuntimePermissions()

        enableEdgeToEdge()

        setContent {
            val viewModel = hiltViewModel<ChatViewModel>()
            val dataBaseViewModel = hiltViewModel<DataBaseViewModel>()
            PersonalHealthAssistantAppTheme {

                val startDestination = remember {
                    if (intent?.getStringExtra("navigate_to") == "snooze") {
                        ScreensName.SnoozeScreen.name
                    } else {
                        ScreensName.HomeScreen.name
                    }
                }
                NavigationGraph(viewModel, dataBaseViewModel)
            }
        }
    }

    private fun requestRuntimePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33)
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+ (API 34+)
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK),
                    2
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }
    }

    @Composable
    fun NavigationGraph(viewModel: ChatViewModel, dataBaseViewModel: DataBaseViewModel) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = ScreensName.SplashScreen.name) {
            composable(ScreensName.SplashScreen.name) {
                SplashScreen(navController)
            }
            composable(ScreensName.ChatBotScreen.name) {
                ChatBotScreen(navController, viewModel = viewModel)
            }
            composable(ScreensName.WelcomeScreen.name) {
                WelcomeScreenFirst(navController)
            }
            composable(ScreensName.WelcomeScreenSecond.name) {
                WelcomeScreenSecond(navController = navController)
            }
            composable(ScreensName.HomeScreen.name) {
                HomeScreen(navController = navController, chatViewModel = viewModel)
            }
            composable(ScreensName.WeightPickerScreen.name) {
                WeightPickerScreen(navController = navController)
            }
            composable(ScreensName.HeightPickerScreen.name) {
                HeightPickerScreen(navController = navController)
            }
            composable(ScreensName.ProfileScreen.name) {
                ProfileScreen(navController = navController)
            }
            composable(ScreensName.LoginScreen.name) {
                LoginSignupScreen(authType = true, navController = navController)
            }
            composable(ScreensName.RegisterScreen.name) {
                LoginSignupScreen(authType = false, navController = navController)
            }
            composable(ScreensName.ForgotPasswordScreen.name) {

            }
            composable(ScreensName.SleepTrackingScreen.name) {
                SleepTrackingScreen(navController, dataBaseViewModel)
            }
            composable(ScreensName.ResetPasswordScreen.name) {

            }
            composable(ScreensName.NotificationSetting.name){
                NotificationSetupScreen()
            }
            composable(ScreensName.HealthTextAnalysisScreen.name){
                HealthTextAnalysisScreen(navController , chatViewModel = viewModel)
            }
            composable(ScreensName.SymptomsInputScreen.name) {
                SymptomsInputScreen(navController, viewModel)
            }
            composable(ScreensName.HydrationScreen.name){
                HydrationRecord(navController = navController)
            }
            composable(ScreensName.HydrationGoalScreen.name){
                WaterGoalSetupScreen(navController = navController)
            }
        }

    }
}
