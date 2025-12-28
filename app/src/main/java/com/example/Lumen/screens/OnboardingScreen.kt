package com.example.Lumen.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.Lumen.data.UserPreferences
import com.example.Lumen.ui.theme.AppTheme

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    var currentStage by remember { mutableStateOf(0) }

    var name by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }

    AnimatedContent(
        targetState = currentStage,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(700))
        },
        label = "onboarding_fade"
    ) { stage ->
        when (stage) {
            0 -> IdentityStage(onNext = { currentStage = 1 })
            1 -> NameStage(
                name = name,
                onNameChange = { name = it },
                onBack = { currentStage = 0 },
                onNext = { if (name.isNotBlank()) currentStage = 2 }
            )
            2 -> OccupationStage(
                occupation = occupation,
                onOccupationChange = { occupation = it },
                onBack = { currentStage = 1 },
                onFinish = {
                    if (occupation.isNotBlank()) {
                        userPrefs.userName = name
                        userPrefs.userOccupation = occupation
                        userPrefs.isOnboardingComplete = true
                        onFinished()
                    }
                }
            )
        }
    }
}

@Composable
fun IdentityStage(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(AppTheme.colors.background).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFEDE0C4).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .border(2.dp, Color(0xFFC0A468), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = AppTheme.colors.fontLogo
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Lumen_", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Scan, Digitise & Organize.\nYour pocket office starts here.",
            fontSize = 16.sp,
            color = Color(0xFFC0A468),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        OnboardingButton(text = "Start", color = Color(0xFFF5E6C4), textColor = Color(0xFF8B7E58), onClick = onNext)
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun NameStage(name: String, onNameChange: (String) -> Unit, onBack: () -> Unit, onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(AppTheme.colors.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Icon(Icons.Outlined.Person, null, tint = AppTheme.colors.fontLogo.copy(alpha=0.5f), modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Text("Create your\nworkspace_", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo, textAlign = TextAlign.Center, lineHeight = 40.sp)

        Spacer(modifier = Modifier.height(12.dp))
        Text("What should we call you?", fontSize = 14.sp, color = Color(0xFFC0A468))

        Spacer(modifier = Modifier.height(40.dp))

        BasicTextField(
            value = name,
            onValueChange = onNameChange,
            textStyle = TextStyle(fontSize = 20.sp, color = AppTheme.colors.fontLogo, textAlign = TextAlign.Center),
            cursorBrush = SolidColor(AppTheme.colors.fontLogo),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(2.dp, Color(0xFFC0A468), RoundedCornerShape(50))
                        .background(Color(0xFFEDE0C4).copy(alpha = 0.3f), RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    if (name.isEmpty()) Text("Enter Name...", color = Color.Gray.copy(alpha = 0.5f))
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                OnboardingButton(text = "Back", color = Color(0xFFFF6B6B), textColor = Color.White, onClick = onBack)
            }
            Box(modifier = Modifier.weight(1f)) {
                OnboardingButton(text = "Next", color = Color(0xFFF5E6C4), textColor = Color(0xFF8B7E58), onClick = onNext)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun OccupationStage(occupation: String, onOccupationChange: (String) -> Unit, onBack: () -> Unit, onFinish: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        OccupationSelectionDialog(
            currentSelection = occupation,
            onDismiss = { showDialog = false },
            onConfirm = { selected ->
                onOccupationChange(selected)
                showDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(AppTheme.colors.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Icon(Icons.Outlined.Work, null, tint = AppTheme.colors.fontLogo.copy(alpha=0.5f), modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Text("Tailor your\nexperience_", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo, textAlign = TextAlign.Center, lineHeight = 40.sp)

        Spacer(modifier = Modifier.height(12.dp))
        Text("What do you do?", fontSize = 14.sp, color = Color(0xFFC0A468))

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp).clickable { showDialog = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .border(2.dp, Color(0xFFC0A468), RoundedCornerShape(50))
                    .background(Color(0xFFEDE0C4).copy(alpha = 0.3f), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (occupation.isEmpty()) "Select Occupation..." else occupation,
                    fontSize = 18.sp,
                    color = AppTheme.colors.fontLogo,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFF5E6C4), RoundedCornerShape(16.dp))
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF8B7E58))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(2.dp, Color(0xFFC0A468)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Back", color = Color(0xFFC0A468), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = onFinish,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA8E6A3)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Finish", color = Color(0xFF3E603B), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun OnboardingButton(text: String, color: Color, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.width(160.dp).height(56.dp)
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun OccupationSelectionDialog(currentSelection: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var tempSelection by remember { mutableStateOf(currentSelection) }
    val occupations = listOf("Data Scientist", "Computer Science", "Software Engineering", "Information Technology", "Cyber Security", "Artificial Intelligence", "Other")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5D0)),
            modifier = Modifier.padding(16.dp).heightIn(max = 500.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(occupations) { item ->
                        val isSelected = tempSelection == item
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .border(1.dp, Color(0xFFC0A468), RoundedCornerShape(50))
                                .clickable { tempSelection = item },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item,
                                color = if (isSelected) Color(0xFF385C77) else Color(0xFF385C77),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onConfirm(tempSelection) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF89CFF0)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(140.dp)
                ) {
                    Text("Confirm", color = Color(0xFF2C4A63), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}