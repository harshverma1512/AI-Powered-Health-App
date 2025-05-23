package com.example.personalhealthassistantapp.utility

import android.content.SharedPreferences
import android.content.Context
import androidx.core.content.edit
import java.time.DayOfWeek
import java.time.LocalDate

class SharedPrefManager(context: Context) {

    private val PREFS_NAME = "MyHealthAppPrefs"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {

        //User Keys
        const val KEY_USERNAME = "username"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val USER_FIRST_TIME_OPEN = "user_first_time_open"
        const val NAME ="fullName"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val ACCOUNT_TYPE  = "accountType"
        const val PHOTO_URL = "photoUrl"
        const val WEIGHT = "weight"
        const val WEIGHT_MEASUREMENT = "weight_measurement"
        const val HEIGHT = "height"
        const val HEALTH_NOTIFY = "health_notify"
        const val HEALTH_ASSISTANT_NOTIFY = "health_assistant_notify"

        // Alarm keys
        const val KEY_SLEEP_HOUR = "sleep_hour"
        const val KEY_SLEEP_MIN = "sleep_min"
        const val KEY_WAKE_HOUR = "wake_hour"
        const val KEY_WAKE_MIN = "wake_min"
        const val SELECTED_DAYS = "selected_days"

        //Water Take
        const val WATER_TAKE = "water_take"
        const val WATER_GOAL = "water_goal"
        const val WATER_UNIT = "water_unit"
        const val TODAY = "today"

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
    fun getWaterTake(): Int = sharedPref.getInt(WATER_TAKE, 0)
    fun getWaterGoal(): Int = sharedPref.getInt(WATER_GOAL, 0)
    fun getWaterUnit() : String = sharedPref.getString("water_unit", "ml") ?: "ml"
    fun getToday() : String = sharedPref.getString(TODAY, LocalDate.now().toString()) ?: LocalDate.now().toString()
    fun getHealthNotify() : Boolean = sharedPref.getBoolean(HEALTH_NOTIFY, true)
    fun getHealthAssistantNotify() : Boolean = sharedPref.getBoolean(HEALTH_ASSISTANT_NOTIFY, false)

    fun saveWakeTime(hour: Int, minute: Int) {
        sharedPref.edit {
            putInt(KEY_WAKE_HOUR, hour)
            putInt(KEY_WAKE_MIN, minute)
        }
    }

    fun saveHealthNotify(healthNotify: Boolean) {
        sharedPref.edit {
            putBoolean(HEALTH_NOTIFY, healthNotify)
        }
    }

    fun saveHealthAssistantNotify(healthAssistantNotify: Boolean) {
        sharedPref.edit {
            putBoolean(HEALTH_ASSISTANT_NOTIFY, healthAssistantNotify)
        }
    }

    fun saveTodayDate(today: LocalDate?) {
        sharedPref.edit {
            putString(TODAY, today.toString())
        }
    }

    fun saveWaterUnit(unit: String) {
        sharedPref.edit {
            putString(WATER_UNIT, unit)
        }
    }

    fun saveWaterTake(waterTake: Int) {
        sharedPref.edit {
            putInt(WATER_TAKE, waterTake)
        }
    }
    fun saveWaterGoal(waterGoal: Int) {
        sharedPref.edit {
            putInt(WATER_GOAL, waterGoal)
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

    fun clearHydrationData() {
        sharedPref.edit(){remove(WATER_TAKE)}
        sharedPref.edit(){remove(WATER_GOAL)}
    }
}
