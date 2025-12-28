package com.example.Lumen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.Lumen.ui.state.ThemeManager
import com.example.Lumen.ui.theme.*

@Composable
fun ThemeSelectionDialog(onDismiss: () -> Unit) {
    // 1. Local state to hold the choice BEFORE applying
    var tempSelectedTheme by remember { mutableStateOf(ThemeManager.currentThemeName) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.defaultCard),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Theme",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.fontLogo
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- ROW 1 ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ThemeOptionCard(
                        name = "Autumn",
                        mainColor = AutumnTheme.defaultCard, // Beige
                        secondaryColor = AutumnTheme.background, // Light Beige
                        isSelected = tempSelectedTheme == "Autumn",
                        modifier = Modifier.weight(1f),
                        onClick = { tempSelectedTheme = "Autumn" }
                    )
                    ThemeOptionCard(
                        name = "Spring",
                        mainColor = SpringTheme.advanceBtn, // Salmon/Red
                        secondaryColor = SpringTheme.background, // Light Orange
                        isSelected = tempSelectedTheme == "Spring",
                        modifier = Modifier.weight(1f),
                        onClick = { tempSelectedTheme = "Spring" }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- ROW 2 ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ThemeOptionCard(
                        name = "Winter",
                        mainColor = WinterTheme.floating, // Blue
                        secondaryColor = WinterTheme.background, // Light Green
                        isSelected = tempSelectedTheme == "Winter",
                        modifier = Modifier.weight(1f),
                        onClick = { tempSelectedTheme = "Winter" }
                    )
                    ThemeOptionCard(
                        name = "Summer",
                        mainColor = SummerTheme.defaultCard, // Orange
                        secondaryColor = SummerTheme.background, // Grey
                        isSelected = tempSelectedTheme == "Summer",
                        modifier = Modifier.weight(1f),
                        onClick = { tempSelectedTheme = "Summer" }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- ACTION BUTTONS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel Button
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.background),
                        border = BorderStroke(1.5.dp, AppTheme.colors.fontLogo),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Cancel", color = AppTheme.colors.fontLogo, fontWeight = FontWeight.Bold)
                    }

                    // Apply Button
                    Button(
                        onClick = {
                            ThemeManager.switchTheme(tempSelectedTheme)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.advanceBtn),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Apply", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeOptionCard(
    name: String,
    mainColor: Color,
    secondaryColor: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Determine background color based on selection state
    // If selected, we might want to highlight it slightly, or just keep the border thick
    val borderThickness = if (isSelected) 2.5.dp else 1.dp
    val backgroundColor = if (isSelected) AppTheme.colors.background else Color.White

    Box(
        modifier = modifier
            .height(80.dp) // Fixed height for consistency
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(borderThickness, AppTheme.colors.fontLogo, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp), // Inner padding
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Theme Name
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = AppTheme.colors.fontLogo
            )

            // The Split Circle Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(mainColor),
                contentAlignment = Alignment.Center
            ) {
                // Half-circle
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterEnd)
                        .background(secondaryColor)
                )
            }
        }
    }
}