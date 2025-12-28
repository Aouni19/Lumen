package com.example.Lumen

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.Box
import com.example.Lumen.data.UserPreferences // Import the new class
import com.example.Lumen.screens.OnboardingScreen
import androidx.compose.ui.Alignment
import com.example.Lumen.ui.theme.TestTheme // Ensure this matches your Theme name
import com.example.Lumen.ui.theme.FontLogo // Import your custom color
import com.example.Lumen.screens.HomeScreen // Import your HomeScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.Lumen.navigation.Screen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.Lumen.components.ScannerBottomBar
import com.example.Lumen.screens.ProfileScreen
import com.example.Lumen.screens.SettingsScreen
import com.example.Lumen.ui.state.ThemeManager
import com.example.Lumen.ui.theme.Background

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeManager.init(applicationContext)
        setContent {
            val context = LocalContext.current
            val userPrefs = remember { UserPreferences(context) }
            var isOnboardingDone by remember { mutableStateOf(userPrefs.isOnboardingComplete) }
            TestTheme { // Make sure this is TestTheme, not ScannerAppTheme
                if (!isOnboardingDone){
                    OnboardingScreen(
                        onFinished = {
                            isOnboardingDone=true
                        }
                    )
                }else{
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        containerColor = Background, // Force your beige background
                        bottomBar = {
                            ScannerBottomBar(currentRoute = currentRoute) { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Document.route,
                            modifier = Modifier.padding(innerPadding),

                            // GLOBAL ANIMATION SETTINGS
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth }, // Slide in from Right
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth }, // Slide out to Left
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> -fullWidth }, // Slide back from Left
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> fullWidth }, // Slide back to Right
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            }
                        ) {
                            // Your composables remain exactly the same!
                            composable(Screen.Document.route) { HomeScreen() }
                            composable(Screen.User.route) { ProfileScreen() }
                            composable(Screen.Settings.route) { SettingsScreen() }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun new() {
    TestTheme  { // Your custom theme from earlier
        val navController = rememberNavController()

        // Track current destination for the Bottom Bar
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                ScannerBottomBar(currentRoute = currentRoute) { route ->
                    navController.navigate(route) {
                        // Pop up to the start of the graph to avoid stacking screens
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        ) { innerPadding ->
            // The NavHost defines which composable shows for which route
            NavHost(
                navController = navController,
                startDestination = Screen.Document.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Document.route) { HomeScreen() }

                composable(Screen.User.route) {
                    // FIX: Removed the Box/Text placeholder and called the actual screen
                    ProfileScreen()
                }

                composable(Screen.Settings.route) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Settings Screen", color = FontLogo)
                    }
                }
            }
        }
    }
}