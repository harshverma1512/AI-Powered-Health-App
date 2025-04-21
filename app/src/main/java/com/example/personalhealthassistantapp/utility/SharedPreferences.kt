package com.example.personalhealthassistantapp.utility

import android.content.SharedPreferences
import android.content.Context
import androidx.core.content.edit

class SharedPrefManager(context: Context) {

    private val PREFS_NAME = "MyHealthAppPrefs"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Keys
    companion object {
        const val KEY_USERNAME = "username"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val USER_FIRST_TIME_OPEN = "user_first_time_open"
    }

    // Save data
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
