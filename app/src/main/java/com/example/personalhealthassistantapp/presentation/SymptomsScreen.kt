package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.R
import com.google.accompanist.flowlayout.FlowRow

@Composable
@Preview(showBackground = true)
fun SymptomsScreen() {
    var text by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf(listOf("Headache", "Muscle Fatigue")) }

    val maxSymptoms = 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Top Bar with progress and Skip
            WeightToolbar()

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Do you have any\nsymptoms/allergy?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder for image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFEAF1FD), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = R.drawable.allergyimage), contentDescription = "")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Symptoms chips & input
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Blue, shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                FlowRow(
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp
                ) {
                    symptoms.forEach { symptom ->
                        AssistChip(
                            onClick = { /* Maybe remove later */ },
                            label = { Text(symptom) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFE5F0FF))
                        )
                    }

                    if (symptoms.size < maxSymptoms) {
                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                                if (it.endsWith(" ")) {
                                    val trimmed = it.trim()
                                    if (trimmed.isNotEmpty() && !symptoms.contains(trimmed)) {
                                        symptoms = symptoms + trimmed
                                    }
                                    text = ""
                                }
                            },
                            placeholder = { Text("Add symptom...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${symptoms.size}/$maxSymptoms",
                    modifier = Modifier.align(Alignment.End),
                    color = Color.Gray
                )
            }
                    Spacer(modifier = Modifier.height(100.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.btn_color))
            ) {
                Text("Continue")
                Spacer(modifier = Modifier.width(10.dp))
                Image(painter = painterResource(id = R.drawable.monotone_arrow_right_md), contentDescription = "")
            }
        }
    }
}
