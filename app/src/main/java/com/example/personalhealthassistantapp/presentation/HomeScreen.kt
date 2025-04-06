package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { TopBarSection() }
            item { HealthScoreSection() }
            item { VitalsSection() }
            item { FitnessTrackerSection() }
            item { WellnessChatbotSection(navController) }
            item { NutritionSection() }
        }

        BottomAppBar(
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Home, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        }
    }
}

@Composable
fun TopBarSection() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Hi, Harsh! 👋", fontWeight = FontWeight.Bold)
                    Text("Good morning 🌞", style = MaterialTheme.typography.bodySmall)
                }
            }
            Icon(Icons.Default.Notifications, contentDescription = null)
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
fun HealthScoreSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Health Score", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("88", fontSize = 32.sp, color = Color(0xFF6C63FF), fontWeight = FontWeight.Bold)
            Text("Awesome! Keep it up.")
        }
    }
}

@Composable
fun VitalsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VitalsCard("Heart Rate", "78.2", Color(0xFF007BFF))
        VitalsCard("BP", "120", Color(0xFFFF4D4F))
        VitalsCard("O₂ Level", "87", Color(0xFF17C964))
    }
}

@Composable
fun VitalsCard(title: String, value: String, color: Color) {
    Row {
        Card(
            modifier = Modifier
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = color)
                Spacer(modifier = Modifier.height(4.dp))
                Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

@Composable
fun FitnessTrackerSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fitness & Activity", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Calories Burned\n• Steps Taken\n• Hydration\n• Sleep")
        }
    }
}

@Composable
fun WellnessChatbotSection(navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable { navController.navigate(ScreensName.ChatBotScreen.name) }
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
fun NutritionSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nutrition Management", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("205 Meals tracked")
        }
    }
}
