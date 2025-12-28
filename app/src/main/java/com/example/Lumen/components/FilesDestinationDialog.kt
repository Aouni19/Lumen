package com.example.Lumen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home // Safe Icon
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
import com.example.Lumen.ui.theme.AppTheme

@Composable
fun FilesDestinationDialog(
    currentSelection: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf(currentSelection) }

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
                Text(
                    text = "Files Destination",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.fontLogo
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Choose where to save documents",
                    fontSize = 14.sp,
                    color = AppTheme.colors.fontLogo.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // OPTION 1: DOWNLOADS
                DestinationOptionItem(
                    title = "Downloads",
                    subtitle = "Internal storage > Downloads",
                    footer = "Default location",
                    isSelected = selectedOption == "Downloads",
                    onClick = { selectedOption = "Downloads" },
                    trailingContent = {
                        // FIX: Replaced crashing system resource with safe Vector Icon
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = AppTheme.colors.fontLogo,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // OPTION 2: DOCUMENTS
                DestinationOptionItem(
                    title = "Documents",
                    subtitle = "Internal storage > Documents",
                    footer = "Subfolder for documents",
                    isSelected = selectedOption == "Documents",
                    onClick = { selectedOption = "Documents" }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // OPTION 3: CUSTOM
                DestinationOptionItem(
                    title = "Custom Folder",
                    subtitle = null,
                    footer = null,
                    isSelected = selectedOption == "Custom",
                    onClick = { selectedOption = "Custom" },
                    trailingContent = {
                        Text(
                            "/Internal storage/...",
                            fontSize = 10.sp,
                            color = AppTheme.colors.fontLogo.copy(alpha = 0.6f)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ACTION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.background),
                        border = BorderStroke(1.5.dp, AppTheme.colors.fontLogo),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Cancel", color = AppTheme.colors.fontLogo, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onSave(selectedOption) },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.advanceBtn),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DestinationOptionItem(
    title: String,
    subtitle: String?,
    footer: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null
) {

    val borderThickness = if (isSelected) 2.5.dp else 1.dp
    val backgroundColor = if (isSelected) AppTheme.colors.background else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(borderThickness, AppTheme.colors.fontLogo, RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // 1. Radio Button
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(20.dp)
                    .border(2.dp, AppTheme.colors.fontLogo, CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppTheme.colors.fontLogo, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Text Column
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AppTheme.colors.fontLogo
                    )

                    // Render Icon or Path Text here if present
                    trailingContent?.invoke()
                }

                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = AppTheme.colors.fontLogo.copy(alpha = 0.7f)
                    )
                }

                if (footer != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = footer,
                        fontSize = 10.sp,
                        color = AppTheme.colors.fontLogo.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}