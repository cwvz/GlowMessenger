package com.example.glowmessager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GlowingDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = DarkBackground,
    primaryContainer = Color(0xFF00363D),
    onPrimaryContainer = NeonCyan,
    secondary = NeonMagenta,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF3D001F),
    onSecondaryContainer = NeonMagenta,
    tertiary = NeonGreen,
    onTertiary = DarkBackground,
    tertiaryContainer = Color(0xFF003D18),
    onTertiaryContainer = NeonGreen,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = DarkBackground
)

@Composable
fun GlowMessagerTheme(
    darkTheme: Boolean = true, // Default to Glowing Dark Theme
    content: @Composable () -> Unit
) {
    val colorScheme = GlowingDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
