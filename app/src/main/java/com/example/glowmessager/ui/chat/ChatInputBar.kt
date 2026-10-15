package com.example.glowmessager.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.SentimentSatisfiedAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glowmessager.ui.theme.DarkBackground
import com.example.glowmessager.ui.theme.DarkSurface
import com.example.glowmessager.ui.theme.DarkSurfaceVariant
import com.example.glowmessager.ui.theme.NeonCyan
import com.example.glowmessager.ui.theme.TextMuted
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.pulsingNeonGlow

@Composable
fun ChatInputBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonCyan
) {
    val isSendEnabled = text.isNotBlank()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Attachment Actions
        IconButton(
            onClick = { /* Media attachment action */ },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.AddCircleOutline,
                contentDescription = "Attach",
                tint = accentColor
            )
        }

        IconButton(
            onClick = { /* Camera action */ },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.CameraAlt,
                contentDescription = "Camera",
                tint = TextMuted
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Text Input Field
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 2.dp),
            placeholder = {
                Text(
                    text = "Type a glowing message...",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            },
            trailingIcon = {
                IconButton(onClick = { /* Emoji picker */ }) {
                    Icon(
                        imageVector = Icons.Rounded.SentimentSatisfiedAlt,
                        contentDescription = "Emoji",
                        tint = TextMuted
                    )
                }
            },
            singleLine = false,
            maxLines = 4,
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                if (isSendEnabled) onSendMessage()
            }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = DarkSurfaceVariant,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Neon Send Button
        val sendButtonModifier = if (isSendEnabled) {
            Modifier.pulsingNeonGlow(
                color = accentColor,
                minAlpha = 0.4f,
                maxAlpha = 0.9f,
                radius = 10.dp,
                cornerRadius = 22.dp
            )
        } else {
            Modifier
        }

        IconButton(
            onClick = onSendMessage,
            enabled = isSendEnabled,
            modifier = sendButtonModifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isSendEnabled) accentColor else DarkSurfaceVariant)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = "Send",
                tint = if (isSendEnabled) DarkBackground else TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
