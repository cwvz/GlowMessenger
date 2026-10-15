package com.example.glowmessager.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glowmessager.data.model.Message
import com.example.glowmessager.data.model.MessageStatus
import com.example.glowmessager.data.model.MessageType
import com.example.glowmessager.data.model.User
import com.example.glowmessager.ui.components.NeonAvatar
import com.example.glowmessager.ui.theme.DarkBubbleOther
import com.example.glowmessager.ui.theme.DarkBubbleSelf
import com.example.glowmessager.ui.theme.DarkSurface
import com.example.glowmessager.ui.theme.DarkSurfaceVariant
import com.example.glowmessager.ui.theme.NeonCyan
import com.example.glowmessager.ui.theme.NeonMagenta
import com.example.glowmessager.ui.theme.TextMuted
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.neonBorder
import com.example.glowmessager.ui.util.ColorUtils
import com.example.glowmessager.ui.util.TimeUtils

@Composable
fun MessageBubble(
    message: Message,
    isSelf: Boolean,
    sender: User?,
    accentColorHex: String?,
    showAvatar: Boolean = true,
    modifier: Modifier = Modifier
) {
    if (message.type == MessageType.SYSTEM) {
        SystemMessagePill(message = message, modifier = modifier)
        return
    }

    val bubbleAccentColor = ColorUtils.parseHexColor(
        hex = if (message.isGlowHighlighted) message.glowColorHex ?: accentColorHex else accentColorHex,
        fallback = if (isSelf) NeonCyan else NeonMagenta
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // Other sender avatar
        if (!isSelf && showAvatar) {
            NeonAvatar(
                name = sender?.name ?: "Cyber",
                avatarUrl = sender?.avatarUrl,
                neonColorHex = sender?.neonColorHex ?: accentColorHex,
                size = 34.dp,
                showGlow = message.isGlowHighlighted
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else if (!isSelf && !showAvatar) {
            Spacer(modifier = Modifier.width(42.dp))
        }

        // Message Content Box
        Column(
            horizontalAlignment = if (isSelf) Alignment.End else Alignment.Start
        ) {
            // Sender name (for receiver in group or bot)
            if (!isSelf && sender != null && showAvatar) {
                Text(
                    text = sender.name,
                    color = bubbleAccentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            val shape = if (isSelf) {
                RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp)
            } else {
                RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp)
            }

            val bubbleModifier = if (message.isGlowHighlighted) {
                Modifier.neonBorder(
                    color = bubbleAccentColor,
                    borderWidth = 1.dp,
                    cornerRadius = 18.dp,
                    glowRadius = 8.dp,
                    glowAlpha = 0.45f
                )
            } else {
                Modifier.border(
                    width = 1.dp,
                    color = if (isSelf) NeonCyan.copy(alpha = 0.3f) else DarkSurfaceVariant,
                    shape = shape
                )
            }

            Box(
                modifier = bubbleModifier
                    .widthIn(max = 280.dp)
                    .clip(shape)
                    .background(if (isSelf) DarkBubbleSelf else DarkBubbleOther)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = message.text,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = TimeUtils.formatMessageTime(message.timestamp),
                            color = TextMuted,
                            fontSize = 10.sp
                        )

                        if (isSelf) {
                            Spacer(modifier = Modifier.width(4.dp))
                            MessageStatusIcon(status = message.status, tintColor = bubbleAccentColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageStatusIcon(status: MessageStatus, tintColor: Color) {
    when (status) {
        MessageStatus.SENDING -> {
            Icon(
                imageVector = Icons.Rounded.AccessTime,
                contentDescription = "Sending",
                tint = TextMuted,
                modifier = Modifier.size(13.dp)
            )
        }
        MessageStatus.SENT -> {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Sent",
                tint = TextMuted,
                modifier = Modifier.size(13.dp)
            )
        }
        MessageStatus.DELIVERED -> {
            Icon(
                imageVector = Icons.Rounded.DoneAll,
                contentDescription = "Delivered",
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
        MessageStatus.READ -> {
            Icon(
                imageVector = Icons.Rounded.DoneAll,
                contentDescription = "Read",
                tint = NeonCyan,
                modifier = Modifier.size(14.dp)
            )
        }
        MessageStatus.FAILED -> {
            Text(
                text = "!",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SystemMessagePill(
    message: Message,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = NeonCyan.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface.copy(alpha = 0.8f))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = message.text,
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
