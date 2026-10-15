package com.example.glowmessager.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AmoledColorScheme = darkColorScheme(
    primary = TextPrimary,
    onPrimary = AmoledBlack,
    primaryContainer = AmoledSurfaceVariant,
    onPrimaryContainer = TextPrimary,
    secondary = TextSecondary,
    onSecondary = AmoledBlack,
    secondaryContainer = AmoledSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    background = AmoledBlack,
    onBackground = TextPrimary,
    surface = AmoledSurface,
    onSurface = TextPrimary,
    surfaceVariant = AmoledSurfaceVariant,
    onSurfaceVariant = TextSecondary
)

@Composable
fun GlowMessagerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = AmoledColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AmoledBlack.toArgb()
            window.navigationBarColor = AmoledBlack.toArgb()
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
