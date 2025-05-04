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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.Utils
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate


@Composable
fun MyMedicationsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    val sampleMedications = listOf(
        Medication("Amoxiciline", "Before Eating", "250mg", true),
        Medication("Losartan", "After eating", "500mg", false),
        Medication("Albuterol", "Before Eating", "1kg", true)
    )

    val laterMeds = listOf(
        Medication("Losartan", "Before Eating", "250mg", false),
        Medication("Omeprazole", "After Eating", "100mg", true)
    )
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
                item {
                    MedicationSection("8:00 AM", sampleMedications)
                    MedicationSection("9:00 AM", laterMeds)
                }
            }
        }
    }
}

    @Composable
    fun MedicationSection(time: String, meds: List<Medication>) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = time, fontWeight = FontWeight.Bold, color = Color(0xFF2D2D2D)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${meds.size} Total", color = Color.Black, fontWeight = FontWeight.Medium
                )
            }

            meds.forEach { medication ->
                MedicationCard(medication)
            }
        }
    }

    @Composable
    fun MedicationCard(med: Medication) {
        var isChecked by remember { mutableStateOf(med.checked) }

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                //  Icon(Icons.Default.Medication, contentDescription = null)

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = med.name, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${med.instructions} • ${med.dosage}",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF007AFF))
                )
            }
        }
    }

data class Medication(
    val name: String,
    val instructions: String,
    val dosage: String,
    val checked: Boolean,
)
