package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import com.example.personalhealthassistantapp.data.model.Medication
import com.example.personalhealthassistantapp.presentation.viewmodel.DataBaseViewModel
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.*
import kotlin.coroutines.CoroutineContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    navController: NavController,
    dataBaseViewModel: DataBaseViewModel
) {
    var name by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Once Daily") }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var mealTiming by remember { mutableStateOf("After") }
    var customInstruction by remember { mutableStateOf("") }
    var autoReminder by remember { mutableStateOf(true) }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val formatter = DateTimeFormatter.ofPattern("MMM dd")
    val frequencies = listOf("Once Daily", "Twice Daily", "3x Per Week", "Every Other Day")

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(it)
                .padding(16.dp)
        ) {
            Utils.BackBtn {
                navController.popBackStack()
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Add Medication",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = name,
                onValueChange = { name = it },
                label = { Text("Medication Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text("Medication Frequency", fontWeight = FontWeight.SemiBold)
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                TextField(value = frequency,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    frequencies.forEach { freq ->
                        DropdownMenuItem(text = { Text(freq) }, onClick = {
                            frequency = freq
                            expanded = false
                        })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            MedicationDurationSection(startDate, endDate, onStartDateClick = {
                showStartPicker = true
            }, onEndDateClick = {
                showEndPicker = true
            }, mealTiming) {
                mealTiming = it
            }

            Spacer(Modifier.height(16.dp))

            Text("Custom Instruction", fontWeight = FontWeight.SemiBold)

            OutlinedTextField(
                value = customInstruction,
                onValueChange = { if (it.length <= 500) customInstruction = it },
                placeholder = { Text("Please remind me when...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5
            )
            Text("${customInstruction.length}/500", modifier = Modifier.align(Alignment.End))

            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F8FF)) // Match background if needed
            ) {
                Text(
                    "Set Auto Reminder?",
                    color = Color(0xFF1B2A4E),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = autoReminder,
                    onCheckedChange = { autoReminder = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF007AFF),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }


            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        dataBaseViewModel.insertMedication(Medication(0, name, customInstruction,frequency,
                            startDate.toString(),
                            endDate.toString(), mealTiming, false))
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth().padding(7.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
            ) {
                Text("Add Medication", color = Color.White)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }

        SharedPrefManager(context = LocalContext.current).setAutoMedicationReminder(autoReminder)

        // Start date picker
        if (showStartPicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(onDismissRequest = { showStartPicker = false }, confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        startDate =
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showStartPicker = false
                }) { Text("OK") }
            }, dismissButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }

        // End date picker
        if (showEndPicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(onDismissRequest = { showEndPicker = false }, confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        endDate =
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showEndPicker = false
                }) { Text("OK") }
            }, dismissButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun MedicationDurationSection(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    mealTiming: String,
    onMealTimingChange: (String) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")
    var selectedCard by remember { mutableStateOf("none") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Medication Duration",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B2A4E)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DurationCard("From", startDate?.format(dateFormatter) ?: "Start Date", onStartDateClick, Modifier.weight(1f))
            DurationCard("From", endDate?.format(dateFormatter) ?: "End Date", onEndDateClick, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Take with Meal?",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B2A4E)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFFFEEF0), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Beta", color = Color(0xFFE0115F), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text("Select 1", fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MealOptionButton(
                selected = mealTiming == "Before",
                text = "Before",
                icon = R.drawable.monotone_arrow_right_md,
                onClick = { onMealTimingChange("Before") },
                modifier = Modifier.weight(1f)
            )
            MealOptionButton(
                selected = mealTiming == "After",
                text = "After",
                icon = R.drawable.monotone_arrow_right_md,
                onClick = { onMealTimingChange("After") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DurationCard(label: String, value: String, onClick: () -> Unit, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B2A4E)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE0E6F1)),
            modifier = Modifier
                .height(50.dp)
                .clickable { onClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img), // replace with calendar icon
                    contentDescription = null,
                    tint = Color(0xFF5E6B87),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = value,
                    color = Color(0xFF1B2A4E),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
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
@Composable
fun MealOptionButton(
    selected: Boolean,
    text: String,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) Color(0xFF007AFF) else Color(0xFFF2F4F7)
    val contentColor = if (selected) Color.White else Color(0xFF1B2A4E)

    Surface(
        modifier = modifier
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        shadowElevation = 1.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, color = contentColor, fontWeight = FontWeight.Medium)
        }
    }
}
