package com.example.personalhealthassistantapp.presentation.welcomScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.ScreensName
import com.example.personalhealthassistantapp.utility.SharedPrefManager

@Composable
fun WelcomeScreenFirst(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Image(painter = painterResource(id = R.drawable.health_plus), contentDescription = "")
        Text(
            text = buildAnnotatedString {
                append("Welcome to\n")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Health Assistant ")
                }
                append("UI Kit")
            },
            style = TextStyle(fontSize = 24.sp),
            fontWeight = Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 15.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.welcome_frame),
            contentDescription = "",
            modifier = Modifier.padding(top = 20.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = {
                if (SharedPrefManager(navController.context).isFirstTimeOpenApp() && !SharedPrefManager(
                        navController.context
                    ).isLoggedIn()
                ) {
                    navController.navigate(ScreensName.WelcomeScreenSecond.name)
                    SharedPrefManager(navController.context).setFirstTimeOpenApp(false)
                } else if (!SharedPrefManager(navController.context).isFirstTimeOpenApp() && SharedPrefManager(
                        navController.context
                    ).isLoggedIn()
                ) {
                    navController.navigate(ScreensName.HomeScreen.name)
                } else {
                    navController.navigate(ScreensName.LoginScreen.name)
                }
            },
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = colorResource(id = R.color.app_bar_color)),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text(text = "Get Started", color = colorResource(id = R.color.white))
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                painter = painterResource(id = R.drawable.monotone_arrow_right_md),
                contentDescription = ""
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account?")
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Sign in",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = Color.Red
            )
        }
    }

}