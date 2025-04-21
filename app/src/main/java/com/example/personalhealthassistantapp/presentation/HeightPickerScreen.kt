package com.example.personalhealthassistantapp.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import kotlinx.coroutines.launch

@Composable
fun HeightPickerScreen(
    modifier: Modifier = Modifier,
    minHeight: Int = 0,
    maxHeight: Int = 300,
    initialHeight: Int = 160,
    navController: NavController,
    onContinue: (Int) -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val pickerHeight = screenHeight * 0.6f
    val itemSpacing = 12.dp
    val itemHeight = 50.dp // Estimate, make sure this matches real item height

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialHeight - minHeight)
    var selectedHeight by remember { mutableIntStateOf(initialHeight) }

    val coroutineScope = rememberCoroutineScope()

    // Collect scroll events and update selectedHeight only when scroll stops
    val density = LocalDensity.current

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    val centerItemIndex = with(density) {
                        listState.firstVisibleItemIndex +
                                if (listState.firstVisibleItemScrollOffset > itemHeight.toPx() / 2) 1 else 0
                    }
                    val newHeight = centerItemIndex + minHeight
                    if (newHeight != selectedHeight) {
                        selectedHeight = newHeight
                        Log.d("checkheight", selectedHeight.toString())
                    }

                    coroutineScope.launch {
                        listState.animateScrollToItem(centerItemIndex)
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround
    ) {
        WeightToolbar(onBackClick = {
            navController.popBackStack()
        }, onSkipClick = {
            navController.navigate(ScreensName.HomeScreen.name)
        })
        Spacer(modifier = Modifier.height(32.dp))

        Text("What is your height?", fontSize = 35.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pickerHeight),
            contentAlignment = Alignment.Center
        ) {
            // The scroller
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(vertical = pickerHeight / 3),
                verticalArrangement = Arrangement.spacedBy(itemSpacing),
                modifier = Modifier.fillMaxSize()
            ) {
                items((minHeight..maxHeight).toList()) { height ->
                    val isSelected = height == selectedHeight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedHeight = height
                                coroutineScope.launch {
                                    listState.animateScrollToItem(height - minHeight)
                                }
                            }
                            .wrapContentHeight()
                            .padding(top = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$height cm",
                            fontSize = if (isSelected) 36.sp else 20.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Color.Black,
                            modifier = if (isSelected) Modifier
                                .background(Color(0xFF2979FF), RoundedCornerShape(12.dp))
                                .padding(horizontal = 30.dp, vertical = 12.dp)
                            else Modifier
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onContinue(selectedHeight)
                navController.navigate(ScreensName.HomeScreen.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.btn_color))
        ) {
            Text("Continue", fontSize = 16.sp)
        }
    }
}
