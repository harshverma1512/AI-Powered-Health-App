package com.example.personalhealthassistantapp.presentation.welcomScreen

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.ScreensName


val list = listOf(
    "Personalize Your Health with Smart AI.",
    "Your Intelligent Fitness Companion.",
    "Emphatic AI Wellness Chatbot For All.",
    "Intuitive Nutrition & Med Tracker with AI",
    "Helpful Resources &  Community."
)

val list2 = listOf(
    "Achieve your wellness goals with our AI-powered platform to your unique needs.",
    "Track your calory & fitness nutrition with AI and get special recommendations.",
    "Experience compassionate and personalized care with our AI chatbot.",
    "Easily track your medication & nutrition with the power of AI.",
    "Join a community of 5,000+ users dedicating to healthy life with AI/ML."
)

val imageList = listOf(
    R.drawable.welcomescreen1,
    R.drawable.welcome_frame2,
    R.drawable.welcome_frame3,
    R.drawable.welcome_frame4,
    R.drawable.welcome_frame5
)

@Composable
fun WelcomeScreenSecond(modifier: Modifier = Modifier, navController: NavController) {

    val whichScreen = remember {
        mutableIntStateOf(0)
    }

    if (whichScreen.intValue <= 4) {
        Column(
            modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {

            Row(
                modifier = modifier
                    .padding(horizontal = 10.dp, vertical = 15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProgressWithSkip()
            }

            Text(
                text = list[whichScreen.intValue],
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.Black,
                style = TextStyle(lineHeight = 40.sp),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = modifier.height(10.dp))

            Text(
                text = list2[whichScreen.value],
                fontSize = 18.sp,
                color = Color.Gray,
                style = TextStyle(lineHeight = 20.sp),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = modifier.weight(1f)) // Push everything up

            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = imageList[whichScreen.intValue]),
                    contentDescription = "welcomeScreen Image",
                    modifier = Modifier.wrapContentHeight()
                )

                Box(
                    modifier = modifier
                        .padding(20.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Row(
                        modifier = modifier
                            .size(60.dp)
                            .background(
                                color = colorResource(id = R.color.btn_color),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Image(painter = painterResource(id = R.drawable.monotone_arrow_right_md),
                            contentDescription = "moving forward",
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxWidth()
                                .clickable {
                                    if (whichScreen.intValue == 4) {
                                        navController.navigate(ScreensName.LoginScreen.name){
                                            popUpTo(ScreensName.WelcomeScreen.name){
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        whichScreen.intValue += 1
                                    }
                                })
                    }
                }
            }
        }
    }
}


@Composable
fun ProgressWithSkip() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Progress Bar
        LinearProgressIndicator(
            progress = 0.2f, // Adjust progress as needed
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)), // Rounded corners
            color = Color(0xFF1A2334), // Progress color
            trackColor = Color(0xFFE0E4EA) // Track color
        )

        // Skip Button
        Text(
            text = "Skip",
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF1A2334),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}