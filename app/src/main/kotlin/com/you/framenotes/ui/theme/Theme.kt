package com.you.framenotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = ForestGreen, onPrimary = CloudWhite,
    secondaryContainer = SkyBlue, onSecondaryContainer = Midnight,
    background = CloudWhite, onBackground = Midnight,
    surface = FreshCream, onSurface = Midnight,
    onSurfaceVariant = StormCloud,
)
private val Dark = darkColorScheme(
    primary = SkyBlue, onPrimary = Midnight,
    secondaryContainer = ForestGreen, onSecondaryContainer = CloudWhite,
    background = Midnight, onBackground = CloudWhite,
    surface = Color(0xFF1D3335), onSurface = CloudWhite,
    onSurfaceVariant = Color(0xFFA8A8A7),
)

@Composable
fun FrameNotesTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = if (dark) Dark else Light, content = content)
