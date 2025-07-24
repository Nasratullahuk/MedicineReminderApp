package e4294395nasratullahuk.medicinereminder

import android.content.Context


object PatientData {

    private const val PREFS_NAME = "MedicineReminderAppPrefs"

    fun saveLoginStatus(context: Context, value: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("APP_LOGIN_STATUS", value).apply() // Updated key
    }

    fun getLoginStatus(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean("APP_LOGIN_STATUS", false) // Updated key
    }

    fun saveUserName(context: Context, name: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("APP_USER_NAME", name).apply()
    }

    fun getUserName(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString("APP_USER_NAME", null) // Updated key
    }

    fun saveUserGender(context: Context, gender: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("APP_USER_GENDER", gender).apply()
    }

    fun getUserGender(context: Context): String? { // Renamed from getGender
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString("APP_USER_GENDER", null) // Updated key
    }

    fun saveUserEmail(context: Context, email: String) { // Renamed from saveEmail
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("APP_USER_EMAIL", email).apply() // Updated key
    }

    fun getUserEmail(context: Context): String? { // Renamed from getEmail
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString("APP_USER_EMAIL", null) // Updated key
    }

    fun clearUserData(context: Context) { // Added a function to clear all stored user data
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }
}