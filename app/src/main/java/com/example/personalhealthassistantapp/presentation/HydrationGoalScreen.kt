package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.SharedPrefManager

@Composable
fun WaterGoalSetupScreen(
    modifier: Modifier = Modifier, navController : NavController
) {
    var selectedUnit by remember { mutableStateOf("mL") }
    var isEditing by remember { mutableStateOf(false) }
    var goalText by remember { mutableStateOf(0) }
    var tempGoalText by remember { mutableStateOf(goalText.toString()) }
    val units = listOf("mL", "L", "US Oz", "UK Oz")
    val context = LocalContext.current
    goalText = SharedPrefManager(context = context).getWaterGoal()
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize().padding(innerPadding)
                .background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your daily goal is",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Units Selector
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    units.forEach { unit ->
                        Button(
                            onClick = { selectedUnit = unit },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedUnit == unit) Color(0xFF2196F3) else Color.White,
                                contentColor = if (selectedUnit == unit) Color.White else Color.Black
                            ),
                            border = BorderStroke(1.dp, Color.LightGray),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(40.dp)
                        ) {
                            Text(text = unit)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Water Glass Image
                Image(
                    painter = painterResource(id = R.drawable.water_bottle),
                    contentDescription = "Glass of Water",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Goal Amount (Editable)
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (isEditing) {
                        TextField(
                            value = tempGoalText,
                            onValueChange = { newText ->
                                if (newText.all { it.isDigit() }) {
                                    tempGoalText = newText
                                }
                            },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold, color = Color.Black
                            ),
                            modifier = Modifier.width(150.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            )
                        )
                    } else {
                        Text(
                            text = goalText.toString(),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = selectedUnit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Adjust Button
                Button(
                    onClick = {
                        if (isEditing) {
                            // Save input
                            goalText = tempGoalText.toIntOrNull() ?: goalText
                        } else {
                            // When entering edit mode, update temp
                            tempGoalText = goalText.toString()
                        }
                        isEditing = !isEditing
                    },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isEditing) "Save" else "Adjust")
                }
            }

            // Bottom Button
            Button(
                onClick = {
                    SharedPrefManager(context = navController.context).saveWaterGoal(goalText)
                    SharedPrefManager(context = context).saveWaterUnit(selectedUnit)
                    navController.navigate(ScreensName.HydrationScreen.name)
                          },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Let's Hydrate!", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
