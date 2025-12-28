package com.example.Lumen.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Lumen.navigation.bottomNavItems
import com.example.Lumen.ui.theme.AppTheme // Import Dynamic Theme

@Composable
fun ScannerBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = AppTheme.colors.defaultCard, // DYNAMIC REPLACEMENT
        tonalElevation = 8.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        bottomNavItems.forEach { screen ->
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen.route) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        // DYNAMIC TINT
                        tint = if (isSelected) AppTheme.colors.fontLogo else AppTheme.colors.fontLogo.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        color = AppTheme.colors.fontLogo, // DYNAMIC REPLACEMENT
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = AppTheme.colors.pillColor, // DYNAMIC REPLACEMENT
                    selectedIconColor = AppTheme.colors.fontLogo,
                    unselectedIconColor = AppTheme.colors.fontLogo.copy(alpha = 0.6f),
                    selectedTextColor = AppTheme.colors.fontLogo
                )
            )
        }
    }
}