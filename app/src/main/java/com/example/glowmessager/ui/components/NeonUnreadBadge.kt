package com.example.glowmessager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glowmessager.ui.theme.DarkBackground
import com.example.glowmessager.ui.theme.NeonMagenta
import com.example.glowmessager.ui.theme.pulsingNeonGlow

@Composable
fun NeonUnreadBadge(
    count: Int,
    badgeColor: Color = NeonMagenta,
    modifier: Modifier = Modifier
) {
    if (count <= 0) return

    val displayText = if (count > 99) "99+" else count.toString()
    val shape = if (count < 10) CircleShape else RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 22.dp, minHeight = 22.dp)
            .pulsingNeonGlow(
                color = badgeColor,
                minAlpha = 0.4f,
                maxAlpha = 0.9f,
                radius = 8.dp,
                cornerRadius = 12.dp
            )
            .clip(shape)
            .background(badgeColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = DarkBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
