package com.example.Lumen.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocalProvider
import com.example.Lumen.ui.state.ThemeManager

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = FontLogo,
    background = Background, // Your 0xFFFAF7F3
    surface = DefaultCard,   // Your 0xFFF0E4D3
    // Force other surfaces to your beige theme
    surfaceVariant = DefaultCard
)



@Composable
fun TestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun TestTheme(content: @Composable () -> Unit) {
    // 1. Get the current theme from our Manager
    val appColors = ThemeManager.currentTheme

    // 2. Map it to Material3 for standard components (like Surface)
    val materialColorScheme = lightColorScheme(
        primary = appColors.fontLogo,
        background = appColors.background,
        surface = appColors.defaultCard,
    )

    // 3. Provide BOTH Material3 AND our Custom colors
    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}