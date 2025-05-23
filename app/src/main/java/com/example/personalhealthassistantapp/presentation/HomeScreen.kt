package com.example.personalhealthassistantapp.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.viewmodel.ChatViewModel
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import com.example.personalhealthassistantapp.utility.Utils.getGreeting
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavController,  chatViewModel: ChatViewModel) {

    LaunchedEffect(Unit) {
        chatViewModel.getUserData()
    }

    Scaffold {innerPadding ->
        val score = chatViewModel.score

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { TopBarSection(navController) }
                item { HealthScoreSection(score) }
                item { VitalsSection(navController) }
                item { FitnessTrackerSection(navController) }
                item { WellnessChatbotSection(navController) }
                item { AllergySymptomsSection(navController) }
             //   item { MedicationSection(navController) }
            }
        }
    }
}

@Composable
fun TopBarSection(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.displayName ?: "User"
    val photoUrl = user?.photoUrl

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ScreensName.ProfileScreen.name)
                        }
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    photoUrl?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Hi, $displayName! 👋", fontWeight = FontWeight.Bold)
                    Text(getGreeting(), style = MaterialTheme.typography.bodySmall)
                }
            }
            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.clickable {
                navController.navigate(ScreensName.NotificationSetting.name)
            })
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search health topics...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors()
        )
    }
}


@Composable
fun HealthScoreSection(score : Int?) {
    val randomColor = remember {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        )
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F7FA))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Score box with loading state
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(randomColor),
                contentAlignment = Alignment.Center
            ) {
                if (score != null) {
                    Text(
                        text = score.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Health Score",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )
                Text(
                    text = getBMIStatusMessage(score?.toDouble() ?: 0.0),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


fun getBMIStatusMessage(bmi: Double): String {
    return when {
        bmi < 16.0 -> "Severely underweight – please consult a healthcare provider."
        bmi < 17.0 -> "Moderately underweight – consider improving your nutrition."
        bmi < 18.5 -> "Mildly underweight – a slight weight gain may be beneficial."
        bmi < 25.0 -> "You are in the healthy weight range – keep up the good lifestyle!"
        bmi < 30.0 -> "You are overweight – consider adopting a more active routine."
        bmi < 35.0 -> "Obese Class I – managing diet and exercise is recommended."
        bmi < 40.0 -> "Obese Class II – seek guidance for healthy weight loss."
        else       -> "Obese Class III – urgent medical advice is highly recommended."
    }
}


@Composable
fun VitalsSection(navController: NavController) {
    Column {
        Text(text = "Upcoming Events", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VitalsCard("Heart Rate", "78.2", Color(0xFF007BFF))
            VitalsCard("BP", "120", Color(0xFFFF4D4F))
            VitalsCard("O₂ Level", "87", Color(0xFF17C964))
        }
    }
}

@Composable
fun VitalsCard(title: String, value: String, color: Color) {
    Row {
        Card(
            modifier = Modifier.width(110.dp),
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, fontWeight = FontWeight.Bold, color = color)
                Spacer(modifier = Modifier.height(4.dp))
                Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

@Composable
fun FitnessTrackerSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Fitness & Activity", fontWeight = FontWeight.Bold)
        SleepCard(navHostController = navController)
        HydrationCard(navController = navController)
        StepsTakenCard(navController =  navController)
    }
}


@Composable
fun StepsTakenCard(
    steps: Int = 1000,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .clickable { navController.navigate(ScreensName.MedicationManagement.name) }
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Left Icon Box
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.walk), // Replace with your walking icon
                        contentDescription = "Steps Icon",
                        tint = Color(0xFF7D8FAB),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Steps Taken",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "You've taken $steps steps.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}


@Composable
fun HydrationCard(
    currentMl: Int = SharedPrefManager(context = LocalContext.current).getWaterTake(),
    goalMl: Int = SharedPrefManager(context = LocalContext.current).getWaterGoal(),
    unit: String = SharedPrefManager(context = LocalContext.current).getWaterUnit(),
    navController: NavController
) {
    val progress = currentMl.toFloat() / convertToMilliliters(goalMl.toFloat(), unit)

    Card(
        modifier = Modifier
            .clickable { navController.navigate(ScreensName.HydrationScreen.name) }
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.drop),
                        contentDescription = "Hydration",
                        tint = Color(0xFF7D8FAB),
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hydration",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Progress bar
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(8) { index ->
                            Box(
                                modifier = Modifier
                                    .height(6.dp)
                                    .weight(1f)
                                    .background(
                                        if (index < (progress * 8).toInt()) Color(0xFF2563EB)
                                        else Color(0xFFE5E7EB), shape = RoundedCornerShape(50)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Progress texts
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$currentMl $unit", // 🆕 show current value with unit
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "$goalMl $unit", // 🆕 show goal value with unit
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SleepCard(
    modifier: Modifier = Modifier,
    progress: Float = 0.26f,
    navHostController: NavController
) {
    Card(
        modifier = modifier
            .clickable { navHostController.navigate(ScreensName.SleepTrackingScreen.name) }
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(12.dp)), // Light grey bg
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.sleep), // Change to custom icon if needed
                        contentDescription = "Sleep",
                        tint = Color(0xFF7D8FAB),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Sleep",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "11/36 Monthly Circadian",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    strokeWidth = 4.dp,
                    color = Color(0xFF8B5CF6),
                    trackColor = Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937)
                )
            }
        }
    }
}


@Composable
fun WellnessChatbotSection(navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { navController.navigate(ScreensName.ChatBotScreen.name) }
            .wrapContentWidth()
            .wrapContentHeight(), // Optional: control height
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // So the image shows fully
    ) {
            Image(
                painter = painterResource(id = R.drawable.chatbotposter),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )
    }
}

@Composable
fun AllergySymptomsSection(navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { navController.navigate(ScreensName.SymptomsInputScreen.name) }
            .wrapContentWidth()
            .wrapContentHeight(), // Optional: control height
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier
            .wrapContentWidth()
            .height(170.dp)){
            Image(
                painter = painterResource(id = R.drawable.allergyimage),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize(),
            )
            Image(painter = painterResource(id = R.drawable.health_plus), contentDescription = "Health Plus Logo", modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp)
                .padding(bottom = 20.dp, end = 20.dp))
        }
    }
}

@Composable
fun MedicationSection(navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(ScreensName.SymptomsInputScreen.name)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nutrition Management", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("205 Meals tracked")
        }
    }
}
