package com.example.Lumen.screens

import android.os.Environment
import android.os.StatFs
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Lumen.components.*
import com.example.Lumen.ui.state.ThemeManager
import com.example.Lumen.ui.theme.AppTheme
import java.text.DecimalFormat

// --- Data & Helper Functions ---
data class StorageData(val usedGb: String, val totalGb: String, val progress: Float)

fun getDeviceStorage(): StorageData {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSizeLong
    val totalBlocks = stat.blockCountLong
    val availableBlocks = stat.availableBlocksLong
    val totalBytes = totalBlocks * blockSize
    val availableBytes = availableBlocks * blockSize
    val usedBytes = totalBytes - availableBytes
    val totalGb = totalBytes.toDouble() / (1024 * 1024 * 1024)
    val usedGb = usedBytes.toDouble() / (1024 * 1024 * 1024)
    val df = DecimalFormat("#")
    return StorageData(
        usedGb = df.format(usedGb),
        totalGb = df.format(totalGb),
        progress = (usedBytes.toFloat() / totalBytes.toFloat())
    )
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val storageInfo = remember { getDeviceStorage() }

    // 1. Initialize SharedPreferences inside the Composable
    val prefs = remember { context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE) }

    // 2. State for Dialog Visibility
    var showThemeDialog by remember { mutableStateOf(false) }
    var showCompressionDialog by remember { mutableStateOf(false) }
    var showDestinationDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    // 3. State for Settings
    var compressionLevel by remember {
        mutableStateOf(prefs.getString("compression_level", "Medium") ?: "Medium")
    }
    var destinationType by remember {
        mutableStateOf(prefs.getString("destination_type", "Downloads") ?: "Downloads")
    }

    // --- POPUP LOGIC ---
    if (showThemeDialog) {
        ThemeSelectionDialog(onDismiss = { showThemeDialog = false })
    }

    if (showCompressionDialog) {
        CompressionDialog(
            currentSelection = compressionLevel,
            onDismiss = { showCompressionDialog = false },
            onApply = { newLevel ->
                compressionLevel = newLevel
                prefs.edit().putString("compression_level", newLevel).apply()
                showCompressionDialog = false
            }
        )
    }

    if (showDestinationDialog) {
        FilesDestinationDialog(
            currentSelection = destinationType,
            onDismiss = { showDestinationDialog = false },
            onSave = { newDest ->
                destinationType = newDest
                prefs.edit().putString("destination_type", newDest).apply()
                showDestinationDialog = false
            }
        )
    }

    if (showAboutDialog) {
        AboutUsDialog(onDismiss = { showAboutDialog = false })
    }

    val gridItemHeight = 110.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.fontLogo,
            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
        )

        // --- REMOVED PREMIUM CARD HERE ---

        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextSettingCard(
                title = "Compression",
                text = compressionLevel,
                modifier = Modifier
                    .weight(1f)
                    .height(gridItemHeight)
                    .clickable { showCompressionDialog = true }
            )
            TextSettingCard(
                title = "Files Destination",
                text = destinationType.replace("Folder", ""),
                modifier = Modifier
                    .weight(1f)
                    .height(gridItemHeight)
                    .clickable { showDestinationDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StorageCapsuleCard(
                used = storageInfo.usedGb,
                total = storageInfo.totalGb,
                progress = storageInfo.progress,
                modifier = Modifier.weight(1f).height(gridItemHeight)
            )

            SettingCard(
                title = "Theme",
                modifier = Modifier
                    .weight(1f)
                    .height(gridItemHeight)
                    .clickable { showThemeDialog = true }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ThemeIcon(
                        primaryColor = AppTheme.colors.fontLogo,
                        secondaryColor = AppTheme.colors.background
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ThemeManager.currentThemeName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.fontLogo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Keep the spacer the same height if you want the grid to look consistent,
            // OR change it to match the new button height.
            Spacer(modifier = Modifier.weight(1f).height(110.dp))

            AboutUsButtonCard(
                onClick = { showAboutDialog = true },
                // CHANGE THIS VALUE (e.g., 80.dp, 120.dp)
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}