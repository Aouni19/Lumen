package com.example.Lumen.ui.state

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.Lumen.ui.theme.*

object ThemeManager {
    // 1. State Variables
    var currentTheme by mutableStateOf(AutumnTheme)
    var currentThemeName by mutableStateOf("Autumn")

    // 2. Private storage reference
    private var prefs: SharedPreferences? = null

    // 3. Called once when App starts (in MainActivity)
    fun init(context: Context) {
        prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        // Load saved name, default to "Autumn" if nothing found
        val savedName = prefs?.getString("saved_theme", "Autumn") ?: "Autumn"

        // Apply it without saving again (to avoid infinite loop)
        applyTheme(savedName)
    }

    // 4. Called when User clicks a button
    fun switchTheme(themeName: String) {
        applyTheme(themeName)
        // Save to phone storage
        prefs?.edit()?.putString("saved_theme", themeName)?.apply()
    }

    // Helper logic to map Name -> Colors
    private fun applyTheme(themeName: String) {
        currentThemeName = themeName
        currentTheme = when (themeName) {
            "Autumn" -> AutumnTheme
            "Summer" -> SummerTheme
            "Winter" -> WinterTheme
            "Spring" -> SpringTheme
            else -> AutumnTheme
        }
    }
}