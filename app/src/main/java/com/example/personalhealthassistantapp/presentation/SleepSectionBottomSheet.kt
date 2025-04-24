package com.example.personalhealthassistantapp.presentation

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.data.model.AlarmModel
import com.example.personalhealthassistantapp.data.model.SleepHistoryModel
import com.example.personalhealthassistantapp.domain.repository.AndroidAlarmScheduler
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepWakeTimePickerUI(
    sleepTime: LocalTime,
    wakeTime: LocalTime,
    onSleepTimeSelected: (LocalTime) -> Unit,
    onWakeTimeSelected: (LocalTime) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TimePickerItem("Sleep From", sleepTime) { selected ->
            onSleepTimeSelected(selected)
        }

        TimePickerItem("Wake Up At", wakeTime) { selected ->
            onWakeTimeSelected(selected)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerItem(
    label: String,
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
) {
    val context = LocalContext.current
    var time by remember { mutableStateOf(initialTime) }

    val hour = time.hour
    val minute = time.minute

    val timePickerDialog = remember {
        TimePickerDialog(
            context, { _, selectedHour, selectedMinute ->
                val selectedTime = LocalTime.of(selectedHour, selectedMinute)
                time = selectedTime
                onTimeSelected(selectedTime)
            }, hour, minute, false
        )
    }

    Column {
        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F8FF),
            border = BorderStroke(1.dp, Color(0xFFE0E6F1)),
            modifier = Modifier
                .height(50.dp)
                .clickable {
                    timePickerDialog.show()
                }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    tint = Color(0xFF5E6B87),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = time.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    color = Color(0xFF1B2A4E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF5E6B87),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerItem(
    label: String,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(initialDate) }

    val datePickerDialog = remember {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val date = LocalDate.of(year, month + 1, dayOfMonth)
            selectedDate = date
            onDateSelected(date)
        }

        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column {
        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F8FF),
            border = BorderStroke(1.dp, Color(0xFFE0E6F1)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { datePickerDialog.show() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF5E6B87),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    color = Color(0xFF1B2A4E),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f)) // 👈 pushes the arrow to the end
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF5E6B87),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterSleepSheetContent(onDismiss: (SleepHistoryModel) -> Unit) {

    val timeDuration = remember { mutableIntStateOf(5) }
    var sleepDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedType by remember { mutableStateOf("Normal") }
    var sleepTime by remember { mutableStateOf(LocalTime.of(23, 0)) }
    var wakeTime by remember { mutableStateOf(LocalTime.of(7, 0)) }
    val context = LocalContext.current
    val selectedDays = remember { mutableStateOf(setOf<DayOfWeek>()) } // Keep track of selected days
    val alarmScheduler = AndroidAlarmScheduler(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Add Sleep Schedule", fontWeight = FontWeight.Bold, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))

        DatePickerItem("Sleep Date", sleepDate) { selected ->
            sleepDate = selected
        }

        Spacer(modifier = Modifier.height(16.dp))

        SleepWakeTimePickerUI(
            sleepTime = sleepTime,
            wakeTime = wakeTime,
            onSleepTimeSelected = { sleepTime = it },
            onWakeTimeSelected = { wakeTime = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sleep Duration", fontWeight = FontWeight.Bold, color = Color.Black)
        Slider(
            value = timeDuration.intValue.toFloat(),
            onValueChange = { timeDuration.intValue = it.toInt() },
            valueRange = 0f..12f,
            steps = 11,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.btn_color),        // Thumb knob
                activeTrackColor = colorResource(R.color.btn_color),  // Filled track
                inactiveTrackColor = Color.LightGray,  // Empty track
                activeTickColor = Color.White, inactiveTickColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sleep Type", fontWeight = FontWeight.Bold, color = Color.Black)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            listOf("Normal", "Post REM", "Power Nap").forEach { tag ->
                SleepTag(
                    text = tag,
                    isSelected = tag == selectedType,
                    onClick = { selectedType = tag }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Auto Repeat Every Day", fontWeight = FontWeight.Bold, color = Color.Black)

        RepeatDaysSelector(
            selectedDays = selectedDays.value,
            onDayToggle = { day ->
                selectedDays.value = selectedDays.value.toMutableSet().apply {
                    if (contains(day)) remove(day) else add(day)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val sharedPrefManager = SharedPrefManager(context)
                sharedPrefManager.saveSleepTime(sleepTime.hour, sleepTime.minute)
                sharedPrefManager.saveWakeTime(wakeTime.hour, wakeTime.minute)

                alarmScheduler.schedule(AlarmModel(LocalDateTime.of(sleepDate, sleepTime), "Sleeping Time"))
                alarmScheduler.schedule(AlarmModel(LocalDateTime.of(sleepDate, wakeTime), "WakeUp Time"))

                onDismiss(
                    SleepHistoryModel(
                        0,
                        sleepDate = sleepDate.toString(),
                        sleepDuration = timeDuration.intValue,
                        sleepType = selectedType
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.btn_color))
        ) {
            Text("Hour Sleep (${timeDuration.intValue})", color = Color.White)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepeatDaysSelector(
    selectedDays: Set<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit
) {
    val context = LocalContext.current

    val today = LocalDate.now().dayOfWeek

    val initialSelectedDays = remember {
        if (selectedDays.isEmpty()) {
            mutableSetOf(today)
        } else {
            selectedDays.toMutableSet()
        }
    }

    val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysOfWeek = DayOfWeek.entries.toTypedArray()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val isSelected = initialSelectedDays.contains(day)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) colorResource(R.color.btn_color) else Color.White)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) colorResource(R.color.btn_color) else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        if (isSelected) {
                            initialSelectedDays.remove(day)
                        } else {
                            initialSelectedDays.add(day)
                        }

                        onDayToggle(day)
                        SharedPrefManager(context).saveSelectedDays(initialSelectedDays)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = dayLabels[index],
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}



@Composable
fun SleepTag(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) colorResource(R.color.btn_color) else Color.LightGray,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp
        )
    }
}

