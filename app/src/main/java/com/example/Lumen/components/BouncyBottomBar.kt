package com.example.Lumen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Lumen.navigation.Screen
import com.example.Lumen.ui.theme.DefaultCard
import com.example.Lumen.ui.theme.FontLogo
import com.example.Lumen.ui.theme.PillColor

@Composable
fun BouncyBottomBar(
    screens: List<Screen>,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Divide width by number of tabs to get precise slot size
    val tabWidth = screenWidth / screens.size

    // Find the index of the current screen to calculate offset
    val selectedIndex = screens.indexOfFirst { it.route == currentRoute }.takeIf { it != -1 } ?: 0

    // THE PHYSICS ENGINE: High bounce, low stiffness for that "wobble"
    val indicatorOffset by animateDpAsState(
        targetValue = tabWidth * selectedIndex,
        animationSpec = spring(
            dampingRatio = 0.5f, // Lower = more wobble (0.5 is very bouncy)
            stiffness = Spring.StiffnessLow // Lower = slower, heavier movement
        ),
        label = "pillAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(DefaultCard) // Your beige theme
    ) {
        // 1. The Moving Pill (Behind the content)
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset) // Moves based on physics!
                .width(tabWidth)
                .fillMaxHeight()
                .padding(vertical = 12.dp, horizontal = 20.dp) // Shrink it a bit
                .clip(RoundedCornerShape(50)) // Pill shape
                .background(PillColor)
        )

        // 2. The Icons and Text (Foreground)
        Row(modifier = Modifier.fillMaxSize()) {
            screens.forEachIndexed { index, screen ->
                val isSelected = index == selectedIndex

                // Clickable Container
                Column(
                    modifier = Modifier
                        .width(tabWidth)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // Remove default ripple for cleaner look
                        ) { onNavigate(screen.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        tint = FontLogo,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = screen.label,
                        color = FontLogo,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}