package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.ui.jakartaSansFontFamily
import com.example.personalhealthassistantapp.utility.Utils

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HydrationBottomSheet(modifier: Modifier = Modifier) {

    Column(
        modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically) {
            Utils.BackBtn {

            }
            Spacer(modifier = modifier.width(20.dp))

            Text(
                text = "Add Water Intake",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = jakartaSansFontFamily
            )
        }
        Spacer(modifier = modifier.height(50.dp))
        HalfCircularWaterTracker(  progress = 250f / 2500f , goal = 250)
    }
}

@Composable
fun HalfCircularWaterTracker(
    progress: Float, // 0f to 1f
    goal: Int,
    currentIntake: Int = 1650,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(25.dp)
            .fillMaxWidth()
            .height(280.dp)
    ) {
        // Half Circular Progress Arc
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20.dp.toPx()
            val startAngle = 180f
            val sweepAngle = 180f

            drawArc(
                color = Color.LightGray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFF2196F3),
                startAngle = startAngle,
                sweepAngle = sweepAngle * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // 👇 This Column stacks Water Drop and Text nicely!
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.drop),
                contentDescription = "Water Drop",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp)) // small space between drop and text

            Text(
                text = "$currentIntake",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "/$goal mL",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Buttons - still Bottom Center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text(text = "Drink (300 mL)")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.water_bottle),
                    contentDescription = "Water Bottle",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}
