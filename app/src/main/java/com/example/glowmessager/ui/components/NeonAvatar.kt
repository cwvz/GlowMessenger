package com.example.glowmessager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.glowmessager.ui.theme.DarkSurfaceVariant
import com.example.glowmessager.ui.theme.NeonBadge
import com.example.glowmessager.ui.theme.NeonCyan
import com.example.glowmessager.ui.theme.OnlineGreen
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.neonCircularGlow
import com.example.glowmessager.ui.util.ColorUtils

@Composable
fun NeonAvatar(
    name: String,
    avatarUrl: String? = null,
    neonColorHex: String? = null,
    isOnline: Boolean = false,
    size: Dp = 50.dp,
    showGlow: Boolean = true,
    modifier: Modifier = Modifier
) {
    val accentColor = ColorUtils.parseHexColor(neonColorHex, NeonCyan)
    val initials = getInitials(name)

    val glowModifier = if (showGlow) {
        modifier.neonCircularGlow(
            color = accentColor,
            radius = size / 4,
            alpha = 0.5f
        )
    } else {
        modifier
    }

    Box(
        modifier = glowModifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Base Avatar Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(DarkSurfaceVariant)
                .border(
                    width = 2.dp,
                    color = accentColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "$name's avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = initials,
                    color = TextPrimary,
                    fontSize = (size.value * 0.36f).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Online Indicator Badge
        if (isOnline) {
            val badgeSize = (size.value * 0.28f).dp.coerceAtLeast(10.dp)
            NeonBadge(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp),
                color = OnlineGreen,
                size = badgeSize,
                isPulsing = true
            )
        }
    }
}

private fun getInitials(name: String): String {
    if (name.isBlank()) return "?"
    val parts = name.trim().split("\\s+".toRegex())
    return when {
        parts.size >= 2 -> "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
        parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
        else -> "?"
    }
}
