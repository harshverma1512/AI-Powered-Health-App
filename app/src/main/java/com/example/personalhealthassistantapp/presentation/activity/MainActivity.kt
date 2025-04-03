package com.example.personalhealthassistantapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personalhealthassistantapp.presentation.ChatBotScreen
import com.example.personalhealthassistantapp.presentation.LoginSignupScreen
import com.example.personalhealthassistantapp.presentation.ScreensName
import com.example.personalhealthassistantapp.presentation.SplashScreen
import com.example.personalhealthassistantapp.presentation.ui.PersonalHealthAssistantAppTheme
import com.example.personalhealthassistantapp.presentation.viewmodel.ChatViewModel
import com.example.personalhealthassistantapp.presentation.welcomScreen.WelcomeScreenFirst
import com.example.personalhealthassistantapp.presentation.welcomScreen.WelcomeScreenSecond
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<ChatViewModel>()
            PersonalHealthAssistantAppTheme {
                NavigationGraph(viewModel)
            }
        }
    }
}

@Composable
fun NavigationGraph(viewModel: ChatViewModel) {
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

        }
        composable(ScreensName.LoginScreen.name) {
            LoginSignupScreen(authType = true , navController = navController)
        }
        composable(ScreensName.RegisterScreen.name) {
            LoginSignupScreen(authType = false, navController = navController)
        }
        composable(ScreensName.ForgotPasswordScreen.name) {

        }
        composable(ScreensName.ResetPasswordScreen.name) {

        }
    }
}
