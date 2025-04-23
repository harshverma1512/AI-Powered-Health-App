package com.example.personalhealthassistantapp.utility

import android.content.SharedPreferences
import android.content.Context
import androidx.core.content.edit
import java.time.DayOfWeek

class SharedPrefManager(context: Context) {

    private val PREFS_NAME = "MyHealthAppPrefs"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        //User Keys
        const val KEY_USERNAME = "username"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val USER_FIRST_TIME_OPEN = "user_first_time_open"

        // Alarm keys
        const val KEY_SLEEP_HOUR = "sleep_hour"
        const val KEY_SLEEP_MIN = "sleep_min"
        const val KEY_WAKE_HOUR = "wake_hour"
        const val KEY_WAKE_MIN = "wake_min"
        const val SELECTED_DAYS = "selected_days"


    }

    fun saveSleepTime(hour: Int, minute: Int) {
        sharedPref.edit {
            putInt(KEY_SLEEP_HOUR, hour)
            putInt(KEY_SLEEP_MIN, minute)
        }
    }

    fun getSleepHour(): Int = sharedPref.getInt(KEY_SLEEP_HOUR, 22)
    fun getSleepMinute(): Int = sharedPref.getInt(KEY_SLEEP_MIN, 0)
    fun getWakeHour(): Int = sharedPref.getInt(KEY_WAKE_HOUR, 6)
    fun getWakeMinute(): Int = sharedPref.getInt(KEY_WAKE_MIN, 0)

    fun saveWakeTime(hour: Int, minute: Int) {
        sharedPref.edit {
            putInt(KEY_WAKE_HOUR, hour)
            putInt(KEY_WAKE_MIN, minute)
        }
    }

    fun saveSelectedDays(selectedDays: Set<DayOfWeek>) {
        val daysStringSet = selectedDays.map { it.name }.toSet()
        sharedPref.edit() { putStringSet(SELECTED_DAYS, daysStringSet) }
    }

    fun getSelectedDays(): Set<DayOfWeek> {
        val daysStringSet = sharedPref.getStringSet(SELECTED_DAYS, emptySet()) ?: emptySet()
        return daysStringSet.map { DayOfWeek.valueOf(it) }.toSet()
    }

    fun saveUsername(username: String) {
        sharedPref.edit() { putString(KEY_USERNAME, username) }
    }

    fun saveLoginStatus(isLoggedIn: Boolean) {
        sharedPref.edit() { putBoolean(KEY_IS_LOGGED_IN, isLoggedIn) }
    }

    fun setFirstTimeOpenApp(isFirstTime: Boolean) {
        sharedPref.edit() { putBoolean(USER_FIRST_TIME_OPEN, isFirstTime) }
    }

    // Get data
    fun getUsername(): String? {
        return sharedPref.getString(KEY_USERNAME, null)
    }

    fun isFirstTimeOpenApp(): Boolean {
        return sharedPref.getBoolean(USER_FIRST_TIME_OPEN, true)
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Clear specific data
    fun clearUserDetails() {
        sharedPref.edit() { remove(KEY_USERNAME) }
        sharedPref.edit() { remove(KEY_IS_LOGGED_IN) }
        sharedPref.edit() { remove(KEY_USERNAME) }
        sharedPref.edit() { remove(KEY_USERNAME) }
    }

    // Clear all data
    fun clearAll() {
        sharedPref.edit() { clear() }
    }
}
