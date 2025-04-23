package com.example.personalhealthassistantapp.presentation.activity

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import com.example.personalhealthassistantapp.presentation.ChatBotScreen
import com.example.personalhealthassistantapp.presentation.HeightPickerScreen
import com.example.personalhealthassistantapp.presentation.HomeScreen
import com.example.personalhealthassistantapp.presentation.LoginSignupScreen
import com.example.personalhealthassistantapp.presentation.ProfileScreen
import com.example.personalhealthassistantapp.presentation.ScreensName
import com.example.personalhealthassistantapp.presentation.SelectAvatarScreen
import com.example.personalhealthassistantapp.presentation.SleepTrackingScreen
import com.example.personalhealthassistantapp.presentation.SplashScreen
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this as Activity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }

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
    }

    @Composable
    fun NavigationGraph(viewModel: ChatViewModel, dataBaseViewModel: DataBaseViewModel) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = ScreensName.HomeScreen.name) {
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
                HomeScreen(navController = navController)
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
            composable(ScreensName.SelectAvatarScreen.name) {
                SelectAvatarScreen(navController = navController)
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
        }

    }
}
