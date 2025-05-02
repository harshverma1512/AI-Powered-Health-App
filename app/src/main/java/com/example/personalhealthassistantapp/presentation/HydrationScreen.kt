package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import com.example.personalhealthassistantapp.utility.Utils

@Composable
fun HydrationRecord(modifier: Modifier = Modifier, navController: NavController) {

    Scaffold { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = colorResource(id = R.color.white))
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Utils.BackBtn {
                    navController.popBackStack()
                }

                Text(
                    text = "Hydration",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Icon(imageVector = Icons.Default.Edit, contentDescription = "", modifier.clickable {
                    navController.navigate(ScreensName.HydrationGoalScreen.name)
                })
            }

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.water_goal),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp * 0.4f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            HalfCircularWaterTracker(
                currentIntake = SharedPrefManager(context = navController.context).getWaterTake().toFloat(),
                goal = convertToMilliliters(
                    SharedPrefManager(navController.context).getWaterGoal().toFloat(),
                    SharedPrefManager(navController.context).getWaterUnit()
                ).toFloat()
            )
        }
    }
}

@Composable
fun HalfCircularWaterTracker(
    currentIntake: Float,
    goal: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var currentIntake by remember { mutableStateOf(currentIntake) }
    val unit = SharedPrefManager(context).getWaterUnit() ?: "mL"
    val progress = currentIntake / goal

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
                sweepAngle = sweepAngle * progress.coerceIn(0f, 1f),
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.drop),
                contentDescription = "Water Drop",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$currentIntake",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "/$goal $unit", // 🆕 Show correct unit
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Bottom Buttons
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
                    onClick = {
                        if (currentIntake < goal) {
                            val addedAmount = when (unit) {
                                "mL" -> 300 // 300 mL
                                "L" -> (0.3f * 1000).toInt() // 0.3 L = 300 mL
                                "US Oz" -> (10f * 29.5735f).toInt() // 10 oz ≈ 296 mL
                                "UK Oz" -> (10f * 28.4131f).toInt() // 10 oz ≈ 284 mL
                                else -> 300 // default fallback
                            }

                            currentIntake += addedAmount
                            SharedPrefManager(context).saveWaterTake(currentIntake.toInt())

                            if (currentIntake >= goal) {
                                showDialog = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text(
                        text = when (unit) {
                            "mL" -> "Drink (300 mL)"
                            "L" -> "Drink (0.3 L)"
                            "US Oz" -> "Drink (10 Oz)"
                            "UK Oz" -> "Drink (10 Oz)"
                            else -> "Drink (300 mL)"
                        }
                    )
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

    // Show dialog when water intake goal is full
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Congratulations!") },
            text = { Text(text = "Water Intake Full 🎉") },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

fun convertToMilliliters(value: Float, unit: String): Int {
    return when (unit) {
        "mL" -> value.toInt()
        "L" -> (value * 1000f).toInt()
        "US Oz" -> (value * 29.5735f).toInt()
        "UK Oz" -> (value * 28.4131f).toInt()
        else -> value.toInt() // fallback to mL
    }
}


