package com.example.Lumen.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// 1. The Data Class defining WHAT colors exist
data class AppColors(
    val background: Color,
    val defaultCard: Color,
    val fontLogo: Color,
    val storageName: Color,
    val docsCreated: Color,
    val sFont: Color,
    val docFont: Color,
    val profileCard: Color,
    val pillColor: Color,
    val floating: Color,
    val advanceBtn: Color
)

// 2. Define the 4 Presets
val AutumnTheme = AppColors(
    background = Color(0xFFFAF7F3),
    defaultCard = Color(0xFFF0E4D3),
    fontLogo = Color(0xFF504416),
    storageName = Color(0xFFFFF1A6),
    docsCreated = Color(0xFFCBF3BB),
    sFont = Color(0xFF668000),
    docFont = Color(0xFF217844),
    profileCard = Color(0xFFCAF4FF),
    pillColor = Color(0xFFFFF1CB),
    floating = Color(0xFFe48f45),
    advanceBtn = Color(0xFFE97777)
)

val SummerTheme = AppColors(
    background = Color(0xFFEBEBEB),
    defaultCard = Color(0xFFFAA533),
    fontLogo = Color(0xFF504416),
    storageName = Color(0xFFFFF1A6),
    docsCreated = Color(0xFFCBF3BB),
    sFont = Color(0xFF668000),
    docFont = Color(0xFF217844),
    profileCard = Color(0xFFCAF4FF),
    pillColor = Color(0xFFF5C857),
    floating = Color(0xFF0BA6DF),    advanceBtn = Color(0xFFE97777)
)

val WinterTheme = AppColors(
    background = Color(0xFFF8FDCF),
    defaultCard = Color(0xFF9BE8D8),
    fontLogo = Color(0xFF504416),
    storageName = Color(0xFFFFF1A6),
    docsCreated = Color(0xFFCBF3BB),
    sFont = Color(0xFF668000),
    docFont = Color(0xFF217844),
    profileCard = Color(0xFFCAF4FF),
    pillColor = Color(0xFFE2F6CA),
    floating = Color(0xFF78C1F3),
    advanceBtn = Color(0xFFE97777)
)

val SpringTheme = AppColors(
    background = Color(0xFFFEEAC9),
    defaultCard = Color(0xFFFDACAC),
    fontLogo = Color(0xFF504416),
    storageName = Color(0xFFFFF1A6),
    docsCreated = Color(0xFFCBF3BB),
    sFont = Color(0xFF668000),
    docFont = Color(0xFF217844),
    profileCard = Color(0xFFCAF4FF),
    pillColor = Color(0xFFFFCDC9),
    floating = Color(0xFFFD7979),
    advanceBtn = Color(0xFFE97777)
)

// 3. The "Magic" Local Provider
val LocalAppColors = staticCompositionLocalOf { AutumnTheme }

// 4. Helper to access colors easily (e.g. AppTheme.colors.background)
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}