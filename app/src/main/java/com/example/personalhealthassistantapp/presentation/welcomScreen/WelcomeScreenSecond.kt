package com.example.personalhealthassistantapp.presentation.welcomScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.presentation.ui.progressColor

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun WelcomeScreenSecond(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Row(modifier = modifier.padding(horizontal = 10.dp, vertical = 15.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween ) {
            CustomLinearProgressIndicator(progress = 0.5f)
            Text(text = "skip", fontSize = 16.sp)
        }
    }
}


@Composable
fun CustomLinearProgressIndicator(progress: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier
        .height(8.dp)
        .width(200.dp)) {
        val width = size.width
        val height = size.height

        // Draw the track
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, height / 2),
            end = Offset(width, height / 2),
            strokeWidth = height,
            cap = StrokeCap.Butt
        )

        // Draw the progress
        drawLine(
            color = progressColor,
            start = Offset(0f, height / 2),
            end = Offset(progress * width, height / 2),
            strokeWidth = height,
            cap = StrokeCap.Butt
        )
    }
}