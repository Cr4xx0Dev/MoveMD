package com.md.move.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassyBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val isBlurSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .then(
                if (enabled && isBlurSupported) {
                    Modifier.blur(15.dp)
                } else {
                    Modifier
                }
            )
            .background(
                MaterialTheme.colorScheme.surface.copy(
                    alpha = if (enabled) 0.4f else 0.9f
                )
            )
    ) {
        content()
    }
}
