package com.example.personalhealthassistantapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Utils {

    const val GEMINI_KEY = "AIzaSyBdEME3G08fi4tMdMLIasIQjeVuUfv2IDE"



    @Composable
    fun WeightToolbar(
        onBackClick: () -> Unit = {},
        onSkipClick: () -> Unit = {},
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(painter = painterResource(id = R.drawable.button_icon),
                contentDescription = "",
                modifier = Modifier.clickable {
                    onBackClick()
                })

            // Simulated progress bar
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .background(Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f) // 40% progress – adjust as needed
                        .background(Color.Black)
                )
            }
            Text(
                text = "Skip",
                modifier = Modifier.padding(start = 16.dp).clickable {
                    onSkipClick()
                },
                color = Color(0xFF1A2334),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}