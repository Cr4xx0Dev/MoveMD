package com.md.move.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    isDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    isBlurEnabled: Boolean,
    onBlurEnabledChange: (Boolean) -> Unit,
    isDynamicIconEnabled: Boolean,
    onDynamicIconEnabledChange: (Boolean) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SettingsSection(title = "Appearance") {
            SettingsToggleItem(
                title = "Dark Mode",
                icon = Icons.Outlined.DarkMode,
                checked = isDarkMode,
                onCheckedChange = onDarkModeChange
            )
            SettingsToggleItem(
                title = "Dynamic Colors (Material You)",
                icon = Icons.Outlined.ColorLens,
                checked = isDynamicColor,
                onCheckedChange = onDynamicColorChange
            )
            SettingsToggleItem(
                title = "Dynamic App Icon",
                icon = Icons.Default.Palette,
                checked = isDynamicIconEnabled,
                onCheckedChange = onDynamicIconEnabledChange
            )
            SettingsToggleItem(
                title = "Glass Effect (Blur)",
                icon = Icons.Outlined.Code,
                checked = isBlurEnabled,
                onCheckedChange = onBlurEnabledChange
            )
        }

        SettingsSection(title = "About") {
            ListItem(
                headlineContent = { Text("MoveMD") },
                supportingContent = { Text("Version 1.2.0") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "made with love by ToxicLemon",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { uriHandler.openUri("https://github.com/ToxicLemon") }) {
                    Icon(
                        imageVector = Icons.Outlined.Code,
                        contentDescription = "GitHub",
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { uriHandler.openUri("https://t.me/ToxicLemon") }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Telegram",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    )
}
