package com.example.personalhealthassistantapp.presentation.welcomScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.personalhealthassistantapp.R

@Composable
@Preview(showBackground = true)
private fun WelcomeScreenFirst() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 30.dp)
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
                .align(Alignment.CenterHorizontally)
        )
        Image(
            painter = painterResource(id = R.drawable.welcome_frame1),
            contentDescription = "",
            modifier = Modifier.padding(top = 20.dp)
        )
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = colorResource(id = R.color.app_bar_color)),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text(text = "Get Started ")
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                painter = painterResource(id = R.drawable.monotone_arrow_right_md),
                contentDescription = ""
            )
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(text = "Already have an account?")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Sign in",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = Color.Red
            )
        }
    }

}