package com.example.glowmessager.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glowmessager.ui.theme.DarkBackground
import com.example.glowmessager.ui.theme.DarkSurface
import com.example.glowmessager.ui.theme.GlowMessagerTheme
import com.example.glowmessager.ui.theme.NeonCyan
import com.example.glowmessager.ui.theme.TextMuted
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.neonBorder
import com.example.glowmessager.ui.theme.pulsingNeonGlow

@Composable
fun EmptyDetailPaneState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
                    .neonBorder(
                        color = NeonCyan,
                        borderWidth = 1.5.dp,
                        cornerRadius = 48.dp,
                        glowRadius = 12.dp,
                        glowAlpha = 0.5f
                    )
                    .pulsingNeonGlow(
                        color = NeonCyan,
                        minAlpha = 0.2f,
                        maxAlpha = 0.6f,
                        radius = 16.dp,
                        cornerRadius = 48.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Chat,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select a Conversation",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pick a chat from the sidebar to view messages, send replies, or chat with CyberBot AI ⚡",
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 800)
@Composable
fun EmptyDetailPaneStatePreview() {
    GlowMessagerTheme {
        EmptyDetailPaneState()
    }
}
