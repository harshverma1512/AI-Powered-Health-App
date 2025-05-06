package com.example.personalhealthassistantapp.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.utility.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: return
    val coroutineScope = rememberCoroutineScope()

    // State for form fields
    var fullName by remember { mutableStateOf(user.displayName ?: "") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user.email ?: "") }
    var selectedAccountType by remember { mutableStateOf("Patient") }
    var imageBase64 by remember { mutableStateOf<String?>(null) } // Base64 string from Firestore
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // Local image before saving

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    // Fetch user data on start
    LaunchedEffect(Unit) {
        user?.let {
            fullName = it.displayName ?: ""
            email = it.email ?: ""

            // Load extended user info from Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        phoneNumber = document.getString("phone") ?: ""
                        selectedAccountType = document.getString("accountType") ?: "Patient"
                        imageBase64 = document.getString("photoUrl")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = colorResource(id = R.color.backgroundColor))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("Profile Setup", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
            launcher.launch("image/*")
        }) {
            Image(
                painter = when {
                    selectedImageUri != null -> rememberAsyncImagePainter(selectedImageUri)
                    imageBase64 != null -> {
                        val bitmap = base64ToBitmap(imageBase64!!)
                        bitmap?.asImageBitmap()?.let { androidx.compose.ui.graphics.painter.BitmapPainter(it) }
                            ?: painterResource(R.drawable.health_plus)
                    }
                    else -> painterResource(R.drawable.health_plus) // Default image
                },
                contentDescription = "Profile",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
                    .size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Account Type", style = MaterialTheme.typography.labelMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Regular", "Patient", "Physician").forEach { type ->
                val isSelected = selectedAccountType == type
                Button(
                    onClick = { selectedAccountType = type },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) colorResource(id = R.color.btn_color) else Color.LightGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(type, color = if (isSelected) Color.White else Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        // Convert selected image to Base64
                        val imageBase64String = selectedImageUri?.let { uri ->
                            uriToBase64(context, uri)
                        } ?: imageBase64

                        // Prepare user data for Firestore
                        val userData = mapOf(
                            SharedPrefManager.NAME to fullName,
                            SharedPrefManager.PHONE to phoneNumber,
                            SharedPrefManager.EMAIL to email,
                            SharedPrefManager.ACCOUNT_TYPE to selectedAccountType,
                            SharedPrefManager.PHOTO_URL to (imageBase64String ?: "")
                        )

                        // Update Firebase Auth profile (display name only)
                        val profileUpdates = userProfileChangeRequest {
                            displayName = fullName
                        }
                        user.updateProfile(profileUpdates).await()

                        // Save user data to Firestore
                        saveUserData(
                            data = userData,
                            onSuccess = {
                                // Update imageBase64 to display the uploaded image immediately
                                if (imageBase64String != null) {
                                    imageBase64 = imageBase64String
                                    selectedImageUri = null // Clear local image
                                }
                                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                // Navigate to WeightPickerScreen
                                navController.navigate(ScreensName.WeightPickerScreen.name)
                            },
                            onError = { e ->
                                Toast.makeText(context, "Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error processing image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.btn_color)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue")
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                painter = painterResource(id = R.drawable.monotone_arrow_right_md),
                contentDescription = ""
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Logout",
            color = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    Firebase.auth.signOut()
                    SharedPrefManager(context).clearUserDetails()
                    navController.navigate(ScreensName.LoginScreen.name) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
            fontWeight = FontWeight.Bold
        )
    }
}

// Helper function to convert Uri to Base64 string
fun uriToBase64(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    // Compress bitmap to reduce size
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Helper function to convert Base64 string to Bitmap
fun base64ToBitmap(base64: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}

fun saveUserData(
    data: Map<String, Any>,
    onSuccess: () -> Unit,
    onError: (e: Exception) -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = Firebase.firestore
    db.collection("users").document(userId)
        .update(data)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener {
            onError(it)
        }
}
