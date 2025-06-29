package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.data.model.Medication
import com.example.personalhealthassistantapp.data.model.SortedMedicationHistoryModel
import com.example.personalhealthassistantapp.presentation.viewmodel.DataBaseViewModel
import com.example.personalhealthassistantapp.utility.Utils
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.coroutines.coroutineContext


@Composable
fun MyMedicationsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dataBaseViewModel: DataBaseViewModel
) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val medicationHistory = remember { mutableStateOf<List<SortedMedicationHistoryModel>>(emptyList()) }

    LaunchedEffect(selectedDate.value) {
        medicationHistory.value = sortMedicationsByDateAndTime(dataBaseViewModel.getAllMedication(), selectedDate.value)
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Utils.BackBtn {
                    navController.popBackStack()
                }

                Text(
                    text = "My Medications",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Icon(imageVector = Icons.Default.Edit, contentDescription = "", modifier.clickable {
                    navController.navigate(ScreensName.AddMedicationScreen.name)
                })
            }


            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(16.dp))

            // Date row
            LazyRow {
                items(7) { i ->
                    val date = LocalDate.now().plusDays(i.toLong())
                    val isSelected = date == selectedDate.value
                    Column(modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFF007AFF) else Color.White)
                        .clickable { selectedDate.value = date }
                        .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.dayOfWeek.name.take(3),
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Gray
                        )
                        Text(
                            text = date.dayOfMonth.toString(),
                            modifier = Modifier.padding(top = 3.dp),
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(medicationHistory.value.size) { index ->
                    MedicationSection(medicationHistory.value[index].time, medicationHistory.value[index].medicationModel)
                }
            }
        }
    }
}

@Composable
fun MedicationSection(time: String?, meds: List<Medication>) {
    val outputFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
    val formattedTime = time?.let {
        try {
            LocalTime.parse(it, DateTimeFormatter.ofPattern("H:mm")).format(outputFormatter)
        } catch (e: Exception) {
            "--"
        }
    } ?: "--"

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.img), // you can use a clock icon
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formattedTime,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D),
                    fontSize = 14.sp
                )
            }

            Text(
                text = "${meds.size} Total",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            meds.forEach { medication ->
                MedicationCard(medication)
            }
        }
    }
}


@Composable
fun MedicationCard(med: Medication) {
    var isChecked by remember { mutableStateOf(med.checked) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.drugs), // Your pill icon
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = med.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${med.instructions} • ${med.dosage}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            isChecked?.let {
                Checkbox(
                    checked = it,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF007AFF),
                        uncheckedColor = Color.LightGray
                    )
                )
            }
        }
    }
}


fun sortMedicationsByDateAndTime(
    medications: List<Medication>,
    currentDate: LocalDate
): List<SortedMedicationHistoryModel> {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH)

    // Step 1: Filter medications where currentDate lies between startDate and endDate
    val filtered = medications.filter { med ->
        val start = med.startDate?.let { LocalDate.parse(it, dateFormatter) }
        val end = med.endDate?.let { LocalDate.parse(it, dateFormatter) }

        start != null && end != null && !currentDate.isBefore(start) && !currentDate.isAfter(end)
    }

    // Step 2: Flatten times into (time, medication) pairs
    val grouped = filtered
        .flatMap { med ->
            med.time.orEmpty().mapNotNull { rawTime ->
                try {
                    val parsedTime = LocalTime.parse(rawTime, timeFormatter)
                    parsedTime to med
                } catch (e: Exception) {
                    null
                }
            }
        }
        .groupBy({ it.first }, { it.second })

    // Step 3: Sort by time
    return grouped.toSortedMap()
        .map { (time, meds) ->
            SortedMedicationHistoryModel(time = time.toString(), medicationModel = meds)
        }
}

