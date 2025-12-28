package com.example.Lumen.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var userName: String
        get() = prefs.getString("user_name", "User") ?: "User"
        set(value) = prefs.edit().putString("user_name", value).apply()

    var userOccupation: String
        get() = prefs.getString("user_occupation", "Explorer") ?: "Explorer"
        set(value) = prefs.edit().putString("user_occupation", value).apply()

    var isOnboardingComplete: Boolean
        get() = prefs.getBoolean("onboarding_complete", false)
        set(value) = prefs.edit().putBoolean("onboarding_complete", value).apply()

    var lifetimePages: Int
        get() = prefs.getInt("lifetime_pages", 0)
        set(value) = prefs.edit().putInt("lifetime_pages", value).apply()

    // 2. Total documents ever created (Never decreases)
    var lifetimeDocs: Int
        get() = prefs.getInt("lifetime_docs", 0)
        set(value) = prefs.edit().putInt("lifetime_docs", value).apply()

    // 3. The timestamp of the VERY FIRST scan (Never changes after set)
    var firstScanTime: Long
        get() = prefs.getLong("first_scan_time", 0L)
        set(value) = prefs.edit().putLong("first_scan_time", value).apply()
}