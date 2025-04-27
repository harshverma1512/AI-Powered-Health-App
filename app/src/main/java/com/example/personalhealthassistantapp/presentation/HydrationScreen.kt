package com.example.personalhealthassistantapp.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.Utils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationRecord(modifier: Modifier = Modifier, navController: NavController) {

    val waterInTakeMl = 1000
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = rememberCoroutineScope()

    Scaffold { innerPadding ->

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false }, sheetState = sheetState
            ) {
                    HydrationBottomSheet()
            }
        }

        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = colorResource(id = R.color.white))
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Utils.BackBtn {
                    navController.popBackStack()
                }
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = "Hydration",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.water_bottle),
                    contentDescription = "WaterBottle",
                    modifier = Modifier.size(50.dp)
                )

                Text(text = "$waterInTakeMl ml", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }

            Text(
                text = "You have achieved your\nStep Goal for today!",
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 15.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            GoalSummaryScreenTwoItems{
                showSheet = true
            }
        }
    }
}

@Composable
fun GoalSummaryScreenTwoItems(onclick: ()->  Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(title = "Total Intake", value = "5,456")
            DividerLine()
            StatItem(title = "Goal", value = "515")
        }


        FloatingActionButton(
            onClick = {
                onclick()
            },
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.health_plus),
                contentDescription = "insert new record",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title, color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp
        )
        Text(
            text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black
        )
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(1.dp)
            .background(Color(0xFFE0E0E0))
    )
}
