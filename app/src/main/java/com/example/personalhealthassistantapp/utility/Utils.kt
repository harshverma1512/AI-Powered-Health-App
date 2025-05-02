package com.example.personalhealthassistantapp.utility


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.domain.services.ReminderWorker
import com.example.personalhealthassistantapp.presentation.ScreensName
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit


object Utils {

    const val GEMINI_KEY = "AIzaSyDcwOYZvyJDffutualkCFrX7lVf6W_lDPI"

    fun saveUserData(data: Map<String, Any>,onSuccess: () -> Unit , onError: (e : Exception) -> Unit) {
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


    private fun scheduleHydrationReminder(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            3, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "HydrationReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun cancelHydrationReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("SleepReminderWork")
    }

    fun setHydrationReminderEnabled(context: Context, enabled: Boolean) {
        if (enabled) {
            scheduleHydrationReminder(context)
        } else {
            cancelHydrationReminder(context)
        }
    }



    fun fetchCurrentUserData(
        onResult: (Map<String, Any>?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val db = Firebase.firestore
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onResult(document.data)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }


    fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning 🌞"
            in 12..16 -> "Good Afternoon ☀️"
            in 17..20 -> "Good Evening 🌇"
            else -> "Good Night 🌙"
        }
    }


    @Composable
    fun BackBtn(
        onBackClick: () -> Unit,
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_icon),
            contentDescription = "",
            modifier = Modifier.clickable {
                onBackClick()
            })
    }
}