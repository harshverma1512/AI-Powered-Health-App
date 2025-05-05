package com.example.personalhealthassistantapp.utility

import android.content.SharedPreferences
import android.content.Context
import androidx.core.content.edit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
        const val HYDRATION_NOTIFY = "hydration_notify"
        const val MEDICATION_ASSISTANT_NOTIFY = "medication_assistant_notify"
        const val MEDICATION_SELECTED = "medication_selected"

        // Alarm keys
        const val KEY_SLEEP_TIME = "sleep_hour_time"
        const val KEY_WAKE_TIME = "wake_hour_time"
        const val SLEEP_SCHEDULE = "sleep_schedule"
        const val SELECTED_DAYS = "selected_days"
        const val SLEEP_DAY = "sleep_day"

        //Water Take
        const val WATER_TAKE = "water_take"
        const val WATER_GOAL = "water_goal"
        const val WATER_UNIT = "water_unit"
        const val HYDRATION_SCHEDULE = "hydration_schedule"
        const val TODAY = "today"

    }

    fun saveSleepTime(time: LocalTime) {
        sharedPref.edit {
            putString(KEY_SLEEP_TIME, time.toString())
        }
    }

    fun getSleepTime() : String? = sharedPref.getString(KEY_SLEEP_TIME, "")
    fun getWakeupTime() : String? = sharedPref.getString(KEY_WAKE_TIME, "")
    fun getSleepDate() : String?= sharedPref.getString(SLEEP_DAY, "")
    fun getWaterTake(): Int = sharedPref.getInt(WATER_TAKE, 0)
    fun getWaterGoal(): Int = sharedPref.getInt(WATER_GOAL, 0)
    fun getWaterUnit() : String = sharedPref.getString(WATER_UNIT, "ml") ?: "ml"
    fun getToday() : String = sharedPref.getString(TODAY, LocalDate.now().toString()) ?: LocalDate.now().toString()
    fun getHydrationNotify() : Boolean = sharedPref.getBoolean(HYDRATION_NOTIFY, false)
    fun getMedicalAssistantNotify() : Boolean = sharedPref.getBoolean(MEDICATION_ASSISTANT_NOTIFY, false)
    fun isMedication(): Boolean = sharedPref.getBoolean(MEDICATION_SELECTED, false)

    fun setMedication(isMedicationSelected : Boolean){
        sharedPref.edit(){
            putBoolean(MEDICATION_SELECTED, isMedicationSelected)
        }
    }

    fun saveSleepDay(localTime: LocalDate){
        sharedPref.edit(){
            putString(SLEEP_DAY, localTime.toString())
        }
    }

    fun saveWakeTime(time: LocalTime) {
        sharedPref.edit {
            putString(KEY_WAKE_TIME, time.toString())
        }
    }

    fun saveHydrationNotify(healthNotify: Boolean) {
        sharedPref.edit {
            putBoolean(HYDRATION_NOTIFY, healthNotify)
        }
    }

    fun setAutoMedicationReminder(autoReminder: Boolean) {
        sharedPref.edit() {putBoolean(MEDICATION_ASSISTANT_NOTIFY, autoReminder)}
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
