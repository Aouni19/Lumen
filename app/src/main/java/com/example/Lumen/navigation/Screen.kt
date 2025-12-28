package com.example.Lumen.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Document : Screen(
        route = "document_screen",
        label = "Document",
        icon = Icons.Default.Description
    )

    object User : Screen(
        route = "user_screen",
        label = "User",
        icon = Icons.Default.Person
    )

    object Settings : Screen(
        route = "settings_screen",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}

val bottomNavItems = listOf(
    Screen.Document,
    Screen.User,
    Screen.Settings
)