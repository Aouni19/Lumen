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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.Lumen.ui.theme.AppTheme

@Composable
fun CompressionDialog(
    currentSelection: String,
    onDismiss: () -> Unit,
    onApply: (String) -> Unit
) {
    // Local state to hold selection before clicking "Apply"
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
                // Header
                Text(
                    text = "Document Compression",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.fontLogo
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Balance quality and file size",
                    fontSize = 14.sp,
                    color = AppTheme.colors.fontLogo.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Options List
                CompressionOptionItem(
                    title = "Low",
                    subtitle = "Best Quality",
                    hint = "Larger file size",
                    isSelected = selectedOption == "Low",
                    onClick = { selectedOption = "Low" }
                )
                Spacer(modifier = Modifier.height(12.dp))
                CompressionOptionItem(
                    title = "Medium",
                    subtitle = "Balanced",
                    hint = "Recommended",
                    isSelected = selectedOption == "Medium",
                    onClick = { selectedOption = "Medium" }
                )
                Spacer(modifier = Modifier.height(12.dp))
                CompressionOptionItem(
                    title = "High",
                    subtitle = "Smaller Size",
                    hint = "Reduced Quality",
                    isSelected = selectedOption == "High",
                    onClick = { selectedOption = "High" }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
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
                        onClick = { onApply(selectedOption) },
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
fun CompressionOptionItem(
    title: String,
    subtitle: String,
    hint: String,
    isSelected: Boolean,
    onClick: () -> Unit
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 1. Custom Radio Button Circle
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(2.dp, AppTheme.colors.fontLogo, CircleShape)
                    .padding(4.dp), // Padding inside to create the gap for the inner dot
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

            // 2. Texts
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppTheme.colors.fontLogo
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = AppTheme.colors.fontLogo.copy(alpha = 0.7f)
                )
            }

            // 3. Right Side Hint
            Text(
                text = hint,
                fontSize = 12.sp,
                // Using a lighter orange/brown tone for the hint
                color = AppTheme.colors.fontLogo.copy(alpha = 0.6f),
                textAlign = TextAlign.End
            )
        }
    }
}