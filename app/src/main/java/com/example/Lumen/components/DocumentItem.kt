package com.example.Lumen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete // New Icon
import androidx.compose.material.icons.filled.Edit   // New Icon
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
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
import com.example.Lumen.ui.theme.AppTheme

@Composable
fun DocumentItem(
    name: String,
    size: String,
    pages: Int,
    uri: String,
    onDelete: () -> Unit,
    onRename: (String) -> Unit,
    onClick: () -> Unit,
    onShare: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }

    if (showRenameDialog) {
        RenameDialog(
            currentName = name,
            onDismiss = { showRenameDialog = false },
            onConfirm = { newName ->
                onRename(newName)
                showRenameDialog = false
            }
        )
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.defaultCard)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = AppTheme.colors.fontLogo, fontWeight = FontWeight.Bold)
                Text("$size • $pages Pages", color = AppTheme.colors.fontLogo.copy(alpha = 0.7f), fontSize = 12.sp)
            }

            // Menu
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = AppTheme.colors.fontLogo)
                }

                MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))) {
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(AppTheme.colors.defaultCard).width(160.dp)
                    ) {
                        // --- 1. SHARE ---
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Share, null, tint = AppTheme.colors.fontLogo, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    MenuText("Share")
                                }
                            },
                            onClick = {
                                showMenu = false
                                onShare()
                            },
                            modifier = Modifier.menuModifier().background(AppTheme.colors.profileCard)
                        )

                        // --- 2. RENAME (Added Edit Icon) ---
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Edit, null, tint = AppTheme.colors.fontLogo, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    MenuText("Rename")
                                }
                            },
                            onClick = {
                                showMenu = false
                                showRenameDialog = true
                            },
                            modifier = Modifier.menuModifier().background(AppTheme.colors.profileCard)
                        )

                        // --- 3. DELETE (Added Trash Icon) ---
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Icon is White to match the white text on the red button
                                    Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    MenuText("Delete", textColor = Color.White)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            modifier = Modifier.menuModifier().background(AppTheme.colors.advanceBtn)
                        )
                    }
                }
            }
        }
    }
}

// --- HELPERS ---
@Composable
fun MenuText(label: String, textColor: Color = AppTheme.colors.fontLogo) {
    Text(
        text = label,
        color = textColor,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start // Changed to Start for better alignment with icons
    )
}

fun Modifier.menuModifier() = this
    .padding(horizontal = 8.dp, vertical = 2.dp)
    .clip(RoundedCornerShape(12.dp))

@Composable
fun RenameDialog(currentName: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var textValue by remember { mutableStateOf(currentName.removeSuffix(".pdf")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Scan", fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo) },
        text = {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Name") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.fontLogo,
                    focusedLabelColor = AppTheme.colors.fontLogo,
                    cursorColor = AppTheme.colors.fontLogo
                )
            )
        },
        confirmButton = { Button(onClick = { onConfirm("$textValue.pdf") }, colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.fontLogo)) { Text("Save", color = Color.White) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = AppTheme.colors.fontLogo) } },
        containerColor = AppTheme.colors.background,
        shape = RoundedCornerShape(24.dp)
    )
}