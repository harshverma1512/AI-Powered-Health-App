package com.example.personalhealthassistantapp.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import com.example.personalhealthassistantapp.utility.Utils.saveUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


@Composable
fun WeightPickerScreen(
    modifier: Modifier = Modifier,
    minWeight: Int = 0,
    maxWeight: Int = 1000,
    initialWeight: Int = 140,
    navController: NavController
) {
    var selectedUnit by remember { mutableStateOf("lbs") }
    var selectedWeight by remember { mutableIntStateOf(initialWeight) }

    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedWeight - minWeight
    )

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty()) return@collect

                // Center of the LazyRow viewport
                val viewportCenter =
                    layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

                // Find the item whose center is closest to the viewport center
                val centerItem = visibleItems.minByOrNull {
                    val itemCenter = it.offset + it.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }

                centerItem?.let {
                    val newSelectedWeight = it.index + minWeight
                    if (selectedWeight != newSelectedWeight) {
                        selectedWeight = newSelectedWeight
                    }
                }
            }
    }


    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize().padding(innerPadding)
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(horizontal = 24.dp), verticalArrangement = Arrangement.SpaceAround
        ) {
            WeightToolbar(onBackClick = {
                navController.popBackStack()
            }, onSkipClick = {
                navController.navigate(ScreensName.HomeScreen.name)
            })

            Text(
                "What is your weight?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = colorResource(id = R.color.white))
                ) {
                    UnitToggleButton("lbs", selectedUnit == "lbs") { selectedUnit = "lbs" }
                    Spacer(modifier = Modifier.width(10.dp))
                    UnitToggleButton("kg", selectedUnit == "kg") { selectedUnit = "kg" }
                }

                Spacer(Modifier.height(150.dp))

                Text(
                    "$selectedWeight $selectedUnit",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )

                Spacer(Modifier.height(16.dp))

                // Scrollable weight scale
                // Scrollable weight scale with better spacing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // Increased height
                ) {
                    LazyRow(
                        state = scrollState,
                        contentPadding = PaddingValues(horizontal = 80.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(vertical = 24.dp) // Add padding to avoid icon collision
                    ) {
                        items((minWeight..maxWeight).toList()) { weight ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(30.dp)
                                        .background(Color.Gray)
                                )
                                Text("$weight", fontSize = 12.sp)
                            }
                        }
                    }

                    // Center indicator (arrow up & down)
                    Icon(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = 10.dp), // Space from top
                        tint = Color.Blue
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.dropup),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = -10.dp), // Space from bottom
                        tint = Color.Blue
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            Button(
                onClick = {

                    val weightData = mapOf(
                        SharedPrefManager.WEIGHT to selectedWeight,
                        SharedPrefManager.WEIGHT_MEASUREMENT to selectedUnit
                    )

                    saveUserData(weightData, onSuccess = {
                        Toast.makeText(
                            navController.context,
                            "Weight updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(ScreensName.HeightPickerScreen.name)
                    }, onError = {
                        Toast.makeText(
                            navController.context,
                            "Failed to update weight: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.btn_color)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue", fontSize = 16.sp)

                Spacer(modifier = Modifier.width(15.dp))

                Image(
                    painter = painterResource(id = R.drawable.monotone_arrow_right_md),
                    contentDescription = ""
                )
            }
        }
    }
}


@Composable
fun WeightToolbar(
    onBackClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable {
                    onSkipClick()
                },
            color = Color(0xFF1A2334),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}


@Composable
fun UnitToggleButton(
    unit: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (selected) colorResource(id = R.color.btn_color) else Color.Transparent
    val contentColor = if (selected) Color.White else Color.DarkGray

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 50.dp, vertical = 13.dp), contentAlignment = Alignment.Center
    ) {
        Text(unit, color = contentColor, fontWeight = FontWeight.SemiBold)
    }
}

