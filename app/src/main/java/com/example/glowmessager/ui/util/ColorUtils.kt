package com.example.glowmessager.ui.util

import androidx.compose.ui.graphics.Color
import com.example.glowmessager.ui.theme.NeonCyan

object ColorUtils {
    fun parseHexColor(hex: String?, fallback: Color = NeonCyan): Color {
        if (hex.isNullOrBlank()) return fallback
        return try {
            val formattedHex = if (hex.startsWith("#")) hex else "#$hex"
            Color(android.graphics.Color.parseColor(formattedHex))
        } catch (e: Exception) {
            fallback
        }
    }
}
