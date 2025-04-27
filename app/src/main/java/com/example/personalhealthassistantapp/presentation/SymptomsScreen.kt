package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.presentation.viewmodel.ChatViewModel
import com.example.personalhealthassistantapp.utility.Utils
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SymptomsInputScreen(navController: NavController , chatViewModel : ChatViewModel) {
    val symptoms = remember { mutableStateListOf<String>() }

    Scaffold {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Utils.BackBtn {
                navController.popBackStack()
            }
            Column {

                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Do you have any\nsymptoms/allergy?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.allergyimage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Chip input box
                SymptomsInputBox(symptoms)
            }

            Button(
                onClick = {
                    navController.navigate(ScreensName.HealthTextAnalysisScreen.name).apply {
                        chatViewModel.setSymptoms(symptoms)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.btn_color)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}

    @Composable
    fun SymptomsInputBox(symptoms: SnapshotStateList<String>) {
        var currentSymptom by remember { mutableStateOf("") }
        val maxSymptoms = 10

        Card(
            border = BorderStroke(2.dp, Color(0xFF3E80FF)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF7FAFF)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                FlowRow(
                    mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp
                ) {
                    symptoms.forEach { symptom ->
                        AssistChip(
                            onClick = { symptoms.remove(symptom) }, // Remove on click
                            label = {
                                Text(
                                    text = symptom,
                                    color = Color(0xFF3E80FF),
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis // optional if text too long
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Color(0xFFDDEBFF)
                            ),
                            modifier = Modifier
                                .defaultMinSize(minHeight = 32.dp)
                                .padding(horizontal = 2.dp) // add padding between chips
                        )
                    }

                    if (symptoms.size < maxSymptoms) {
                        OutlinedTextField(
                            value = currentSymptom,
                            onValueChange = { newText ->
                                if (newText.endsWith(" ") || newText.endsWith("\n")) {
                                    if (currentSymptom.isNotBlank()) {
                                        symptoms.add(currentSymptom.trim())
                                    }
                                    currentSymptom = ""
                                } else {
                                    currentSymptom = newText
                                }
                            },
                            placeholder = { Text("Type symptom") },
                            modifier = Modifier
                                .widthIn(min = 80.dp, max = 200.dp)
                                .padding(4.dp)
                                .defaultMinSize(minHeight = 40.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${symptoms.size}/$maxSymptoms", color = Color.Gray, fontSize = 12.sp
                    )
                }
            }
        }
    }
