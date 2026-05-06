package com.md.move

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.md.move.ui.screens.FahrplanScreen
import com.md.move.ui.screens.NewsScreen
import com.md.move.ui.screens.SettingsScreen
import com.md.move.ui.theme.MoveMDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(systemDark) }
            var dynamicColor by remember { mutableStateOf(true) }
            var blurEnabled by remember { mutableStateOf(true) }
            var dynamicIconEnabled by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableIntStateOf(0) }

            MoveMDTheme(
                darkTheme = darkTheme,
                dynamicColor = dynamicColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(
                        currentScreen = currentScreen,
                        onScreenChange = { currentScreen = it },
                        isDarkMode = darkTheme,
                        onDarkModeChange = { darkTheme = it },
                        isDynamicColor = dynamicColor,
                        onDynamicColorChange = { dynamicColor = it },
                        isBlurEnabled = blurEnabled,
                        onBlurEnabledChange = { blurEnabled = it },
                        isDynamicIconEnabled = dynamicIconEnabled,
                        onDynamicIconEnabledChange = { dynamicIconEnabled = it }
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    isDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    isBlurEnabled: Boolean,
    onBlurEnabledChange: (Boolean) -> Unit,
    isDynamicIconEnabled: Boolean,
    onDynamicIconEnabledChange: (Boolean) -> Unit
) {
    Scaffold(
        bottomBar = {
            FloatingBottomBar(currentScreen, onScreenChange, isBlurEnabled)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                0 -> FahrplanScreen()
                1 -> NewsScreen()
                2 -> SettingsScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange,
                    isDynamicColor = isDynamicColor,
                    onDynamicColorChange = onDynamicColorChange,
                    isBlurEnabled = isBlurEnabled,
                    onBlurEnabledChange = onBlurEnabledChange,
                    isDynamicIconEnabled = isDynamicIconEnabled,
                    onDynamicIconEnabledChange = onDynamicIconEnabledChange
                )
            }
        }
    }
}

@Composable
fun FloatingBottomBar(
    selectedScreen: Int,
    onScreenChange: (Int) -> Unit,
    isBlurEnabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isBlurEnabled) 0.6f else 1f),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationItem(
                    icon = Icons.Default.Train,
                    label = "Fahrplan",
                    selected = selectedScreen == 0,
                    onClick = { onScreenChange(0) }
                )
                NavigationItem(
                    icon = Icons.Default.Newspaper,
                    label = "News",
                    selected = selectedScreen == 1,
                    onClick = { onScreenChange(1) }
                )
                NavigationItem(
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    selected = selectedScreen == 2,
                    onClick = { onScreenChange(2) }
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
