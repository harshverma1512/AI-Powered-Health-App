package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.R

@Composable
@Preview(showBackground = true)
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.health_plus),
            contentDescription = "AppLogo",
        )
        Text(text = "Personal Health Assistant", color = Color.Black,fontSize = 24.sp, modifier = Modifier.padding(vertical = 18.dp), fontWeight = Bold)
        Text(text = "Your Intelligent AI Health & \n Fitness Assistant Wellness Companion.", fontSize = 16.sp, textAlign = TextAlign.Center)

    }
}