package com.example.Lumen.screens

import android.os.Build
import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.Lumen.data.HomeViewModel
import com.example.Lumen.data.UserPreferences
import com.example.Lumen.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current

    val userPrefs = remember { UserPreferences(context) }
    val userName = remember { userPrefs.userName }
    val occupation = remember { userPrefs.userOccupation }

    val totalPages by viewModel.totalPageCount.collectAsState()
    val avgPages by viewModel.averagePages.collectAsState()
    val journeyStartTimestamp by viewModel.journeyStartTime.collectAsState()

    val avgFormatted = "%.1f".format(avgPages)
    val lastActiveText = "Today"

    val manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
    val deviceInfo = "$manufacturer ${Build.MODEL} • Android ${Build.VERSION.RELEASE}"

    val journeyText = remember(journeyStartTimestamp) {
        if (journeyStartTimestamp == 0L) "Not started yet"
        else {
            val diff = System.currentTimeMillis() - journeyStartTimestamp
            val days = (diff / DateUtils.DAY_IN_MILLIS).toInt()
            when {
                days < 1 -> "Today!"
                days < 30 -> "$days days ago"
                else -> "${days / 30} months ago"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.fontLogo,
            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(BlobShape())
                    .background(AppTheme.colors.profileCard),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = AppTheme.colors.fontLogo.copy(alpha = 0.8f)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoSmallCard(
                    label = "Name",
                    value = userName,
                    bgColor = AppTheme.colors.storageName,
                    textColor = AppTheme.colors.fontLogo
                )
                InfoSmallCard(
                    label = "Occupation",
                    value = occupation,
                    bgColor = AppTheme.colors.docsCreated,
                    textColor = AppTheme.colors.docFont
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Analytics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.fontLogo,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatGridItem(
                icon = Icons.Default.Description,
                label = "Total Pages",
                value = totalPages.toString(),
                containerColor = AppTheme.colors.docsCreated,
                contentColor = AppTheme.colors.docFont,
                modifier = Modifier.weight(1f)
            )
            StatGridItem(
                icon = Icons.Default.Functions,
                label = "Avg. Pages",
                value = avgFormatted,
                containerColor = AppTheme.colors.docsCreated,
                contentColor = AppTheme.colors.docFont,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        StatWideItem(
            icon = Icons.Default.AccessTime,
            label = "Last Active",
            value = lastActiveText
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatWideItem(
            icon = Icons.Default.Smartphone,
            label = "Device Info",
            value = deviceInfo
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Insights",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.fontLogo,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        HighlightCard("You've digitized $totalPages pages so far!")
        HighlightCard("Your journey started $journeyText")

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun StatGridItem(
    icon: ImageVector,
    label: String,
    value: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = value,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun StatWideItem(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.defaultCard)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.fontLogo.copy(alpha = 0.8f),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontSize = 12.sp, color = AppTheme.colors.fontLogo.copy(alpha = 0.7f))
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun InfoSmallCard(label: String, value: String, bgColor: Color, textColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp, 10.dp)) {
            Text(label, fontSize = 11.sp, color = textColor.copy(alpha = 0.8f))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun HighlightCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.profileCard.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.fontLogo.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = AppTheme.colors.fontLogo.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontWeight = FontWeight.Medium, color = AppTheme.colors.fontLogo, fontSize = 14.sp)
        }
    }
}

class BlobShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val w = size.width
            val h = size.height
            moveTo(w * 0.2f, 0f)
            quadraticTo(w * 0.5f, -h * 0.1f, w * 0.8f, 0f)
            quadraticTo(w * 1.1f, h * 0.3f, w, h * 0.6f)
            quadraticTo(w * 0.9f, h * 1.1f, w * 0.5f, h)
            quadraticTo(w * 0.1f, h * 1.1f, 0f, h * 0.6f)
            quadraticTo(-w * 0.1f, h * 0.3f, w * 0.2f, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}