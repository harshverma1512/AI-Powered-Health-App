package com.example.personalhealthassistantapp.presentation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.collection.mutableObjectIntMapOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextPainter.paint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import com.example.personalhealthassistantapp.presentation.viewmodel.ChatViewModel
import com.example.personalhealthassistantapp.presentation.viewmodel.DataBaseViewModel
import com.example.personalhealthassistantapp.utility.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepTrackingScreen(navigation: NavController, dataBaseViewModel : DataBaseViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    val context = rememberCoroutineScope()

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            FilterSleepSheetContent(
                onDismiss = { timeDuration ->
                    context.launch {
                        dataBaseViewModel.insertSleepHistory(
                            SleepHistoryModel(
                                sleepDuration = timeDuration,
                                sleepType = "Normal",
                                sleepDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                sleepStatus = "ok"
                            )
                        )
                    }
                    showSheet = false
                },
                dataBaseViewModel = dataBaseViewModel
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF7F9FC))
                .padding(16.dp)
        ) {
            Utils.BackBtn { navigation.popBackStack() }
            NativeCalendar{
                currentDate = it
            }
            Spacer(modifier = Modifier.height(16.dp))
            SleepOverviewCard()
            Spacer(modifier = Modifier.height(16.dp))
            SleepHistorySection()
            Spacer(modifier = Modifier.height(80.dp)) // for FAB spacing
        }

        FloatingActionButton(
            onClick = { showSheet = true },
            containerColor = Color(0xFF3B82F6),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}


@Composable
fun SleepHistorySection() {
    Column {
        Text("Sleep History", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        val sampleHistory = listOf(
            "You slept for 4.2h" to "Too short",
            "You slept for 7.8h" to "Recommended",
            "You slept for 5.1h" to "OK",
            "You slept for 6.2h" to "Average"
        )

        sampleHistory.forEachIndexed { index, item ->
            SleepHistoryCard(day = 27 - index, text = item.first, tag = item.second)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SleepHistoryCard(day: Int, text: String, tag: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("July $day", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Text(text, fontWeight = FontWeight.Bold)
            }
            Text(
                tag,
                color = when (tag) {
                    "Too short" -> Color.Red
                    "Recommended" -> Color(0xFF10B981)
                    "OK" -> Color(0xFF6366F1)
                    else -> Color.Gray
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SleepOverviewCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Sleep Overview", color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("116+", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Icon(
                painter = painterResource(id = R.drawable.night), // Add moon/star image
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NativeCalendar(selectionDate : (LocalDate) -> Unit) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7
    val days = (1..daysInMonth).map { currentMonth.atDay(it) }

    Column(modifier = Modifier.padding(16.dp)) {

        // Month Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                TextButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Text("< Prev", color = Color.Black)
                }
                TextButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Text("Next >", color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Weekday Headers
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Calendar Grid
        val totalCells = firstDayOfWeek + days.size
        val rows = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (column in 0..6) {
                        val cellIndex = row * 7 + column
                        val date = if (cellIndex >= firstDayOfWeek && cellIndex - firstDayOfWeek < days.size) {
                            days[cellIndex - firstDayOfWeek]
                        } else null

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(MaterialTheme.shapes.medium)
                                .background(
                                    when {
                                        date == selectedDate -> colorResource(R.color.app_bar_color)
                                        else -> Color.Transparent
                                    }
                                )
                                .clickable(enabled = date != null) {
                                    selectedDate = date!!
                                    selectionDate.invoke(date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date?.dayOfMonth?.toString() ?: "",
                                color = if (date == selectedDate) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem(color = colorResource(R.color.app_bar_color), label = "Normal")
            LegendItem(color = colorResource(R.color.light_red), label = "Insomniac")
            LegendItem(color = Color.Yellow, label = "Irregular")
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = RoundedCornerShape(2.dp))
        )
        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
fun FilterSleepSheetContent(onDismiss: (Int) -> Unit , dataBaseViewModel : DataBaseViewModel) {

    val timeDuration = remember { mutableIntStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Filter Sleep", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value = "20/15/2052", onValueChange = {}, label = { Text("From") }, modifier = Modifier.weight(1f), leadingIcon = {
                Image(painterResource(R.drawable.calendar), contentDescription = null, modifier = Modifier.size(18.dp))
            })
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(value = "20/15/2052", onValueChange = {}, label = { Text("To") }, modifier = Modifier.weight(1f), leadingIcon = {
                Image(painterResource(R.drawable.calendar), contentDescription = null, modifier = Modifier.size(18.dp))
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sleep Duration", fontWeight = FontWeight.Bold, color = Color.Black)
        Slider(
            value = timeDuration.intValue.toFloat(),
            onValueChange = {timeDuration.intValue = it.toInt()},
            valueRange = 0f..12f,
            steps = 11, colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.btn_color),        // Thumb knob
                activeTrackColor = colorResource(R.color.btn_color),  // Filled track
                inactiveTrackColor = Color.LightGray,  // Empty track
                activeTickColor = Color.White,
                inactiveTickColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sleep Type", fontWeight = FontWeight.Bold , color = Color.Black)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            SleepTag("Normal", Color.LightGray)
            SleepTag("Post REM", colorResource(R.color.btn_color))
            SleepTag("Power Nap", Color.LightGray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                    onDismiss(timeDuration.intValue)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.btn_color))
        ) {
            Text("Hour Sleep (${timeDuration.intValue})", color = Color.White)
        }
    }
}

@Composable
fun SleepTag(text: String, background: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}
