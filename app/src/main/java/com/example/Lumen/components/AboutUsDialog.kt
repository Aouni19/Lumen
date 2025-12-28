package com.example.Lumen.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.Lumen.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.Lumen.ui.theme.AppTheme

@Composable
fun AboutUsDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    // Define the Kotlin logo vector path
    val kotlinIcon = rememberKotlinIcon()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.background),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- 1. Header ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.advanceBtn)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "About Us",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- 2. Profile Picture Placeholder ---
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.defaultCard) // Placeholder color
                        // Add your Image() composable here later
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- 3. Name & Role ---
                    Text(
                        text = "Aoun Raza",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.fontLogo
                    )
                    Text(
                        text = "Developer of this App",
                        fontSize = 14.sp,
                        color = AppTheme.colors.fontLogo.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 4. Links Block ---
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.defaultCard),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            // Language Row
                            AboutLinkRow(
                                icon = { Icon(imageVector = kotlinIcon, contentDescription = "Kotlin", tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                                text = "Language",
                                trailingContent = {
                                    PillButton(text = "Kotlin", icon = Icons.Default.ChevronRight)
                                }
                            )
                            Divider(color = AppTheme.colors.fontLogo.copy(alpha = 0.1f))

                            // LinkedIn Row
                            AboutLinkRow(
                                icon = {
                                    // Replace R.drawable.ic_linkedin with your actual drawable resource
                                    Icon(painter = painterResource(id = R.drawable.ic_linkedin), contentDescription = "LinkedIn", tint = Color(0xFF0077B5), modifier = Modifier.size(20.dp))
                                },
                                text = "LinkedIn",
                                trailingContent = {
                                    PillButton(text = "Connect", icon = Icons.Default.ChevronRight, onClick = { openUrl(context, "https://www.linkedin.com/in/aoun-raza-is-cool") })
                                }
                            )
                            Divider(color = AppTheme.colors.fontLogo.copy(alpha = 0.1f))

                            // GitHub Row
                            AboutLinkRow(
                                icon = {
                                    // Replace R.drawable.ic_github with your actual drawable resource
                                    Icon(painter = painterResource(id = R.drawable.ic_github), contentDescription = "GitHub", tint = Color.Black, modifier = Modifier.size(20.dp))
                                },
                                text = "GitHub",
                                trailingContent = {
                                    PillButton(text = "Follow", icon = Icons.Default.ChevronRight, onClick = { openUrl(context, "https://github.com/Aouni19") })
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 5. Footer "Made with 💛" ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Divider(modifier = Modifier.weight(1f).padding(end = 8.dp), color = AppTheme.colors.fontLogo.copy(alpha = 0.1f))
                        Text("Made with 💛", fontSize = 12.sp, color = AppTheme.colors.fontLogo.copy(alpha = 0.6f))
                        Divider(modifier = Modifier.weight(1f).padding(start = 8.dp), color = AppTheme.colors.fontLogo.copy(alpha = 0.1f))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 6. Close Button ---
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.advanceBtn),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Close", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AboutLinkRow(
    icon: @Composable () -> Unit,
    text: String,
    trailingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo)
        }
        trailingContent()
    }
}

@Composable
fun PillButton(text: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.background),
        border = BorderStroke(1.dp, AppTheme.colors.fontLogo.copy(alpha = 0.2f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = 12.sp, color = AppTheme.colors.fontLogo, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = icon, contentDescription = null, tint = AppTheme.colors.fontLogo, modifier = Modifier.size(14.dp))
        }
    }
}

// Helper to open URLs
fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

// Kotlin Icon Vector (You don't need to touch this)
@Composable
fun rememberKotlinIcon(): ImageVector {
    return androidx.compose.runtime.remember {
        ImageVector.Builder(
            name = "kotlin_icon",
            defaultWidth = 800.0.dp,
            defaultHeight = 800.0.dp,
            viewportWidth = 800.0f,
            viewportHeight = 800.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF7F52FF)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(800.0f, 800.0f)
                lineToRelative(-800.0f, 0.0f)
                lineToRelative(0.0f, -800.0f)
                lineToRelative(800.0f, 0.0f)
                lineToRelative(0.0f, 800.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFC711E1)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(800.0f, 0.0f)
                lineToRelative(400.0f, 400.0f)
                lineToRelative(-400.0f, 400.0f)
                lineToRelative(-400.0f, -400.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFF88909)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(400.0f, 0.0f)
                lineToRelative(400.0f, 400.0f)
                lineToRelative(-400.0f, 400.0f)
                lineToRelative(-400.0f, -400.0f)
                close()
            }
        }.build()
    }
}