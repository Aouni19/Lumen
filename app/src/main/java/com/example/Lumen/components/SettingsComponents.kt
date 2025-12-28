package com.example.Lumen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Lumen.ui.theme.AppTheme

// 1. A Flexible Setting Card that takes any Content
@Composable
fun SettingCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.defaultCard)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.fontLogo,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Render the custom content (Text, Icon, etc.)
                content()
            }
        }
    }
}

// 2. Helper for simple Text Cards (like "Compression")
@Composable
fun TextSettingCard(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    SettingCard(title, modifier) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppTheme.colors.fontLogo,
            textAlign = TextAlign.Center
        )
    }
}

// 3. The Custom "Split Circle" Icon
@Composable
fun ThemeIcon(
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(primaryColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterEnd)
                .background(secondaryColor)
        )
    }
}

@Composable
fun StorageCapsuleCard(
    used: String,
    total: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AppTheme.colors.defaultCard)
            .clickable { /* Handle Click */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress)
                .background(AppTheme.colors.fontLogo.copy(alpha = 0.2f))
        )
        Box(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Storage-",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.fontLogo
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$used/${total}gb",
                    fontSize = 20.sp,
                    color = AppTheme.colors.fontLogo.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun AboutUsButtonCard( // Renamed from AdvanceButtonCard
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        // Use the red advanceBtn color
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.advanceBtn)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "About Us", // Changed text
                fontSize = 20.sp,  // Increased size slightly
                fontWeight = FontWeight.Bold,
                color = Color.White // Changed to white text
            )
        }
    }
}