package com.example.glowmessager.ui.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.glowmessager.data.model.Conversation
import com.example.glowmessager.ui.components.NeonAvatar
import com.example.glowmessager.ui.theme.DarkBackground
import com.example.glowmessager.ui.theme.DarkBubbleOther
import com.example.glowmessager.ui.theme.DarkSurfaceVariant
import com.example.glowmessager.ui.theme.GlowMessagerTheme
import com.example.glowmessager.ui.theme.NeonCyan
import com.example.glowmessager.ui.theme.NeonMagenta
import com.example.glowmessager.ui.theme.TextMuted
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.TextSecondary
import com.example.glowmessager.ui.theme.pulsingNeonGlow
import com.example.glowmessager.ui.util.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    LaunchedEffect(conversationId) {
        viewModel.loadConversation(conversationId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new messages
    LaunchedEffect(uiState.messages.size, uiState.isTyping) {
        val totalCount = uiState.messages.size + if (uiState.isTyping) 1 else 0
        if (totalCount > 0) {
            listState.animateScrollToItem(totalCount - 1)
        }
    }

    val conversation = uiState.conversation
    val accentColor = ColorUtils.parseHexColor(conversation?.accentColorHex, NeonCyan)
    val otherUser = conversation?.participants?.firstOrNull { it.id != uiState.currentUser?.id }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = accentColor
                        )
                    }
                },
                title = {
                    if (conversation != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NeonAvatar(
                                name = conversation.title,
                                avatarUrl = otherUser?.avatarUrl,
                                neonColorHex = conversation.accentColorHex,
                                isOnline = otherUser?.isOnline == true || conversation.id == "conv_bot",
                                size = 40.dp,
                                showGlow = true
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = conversation.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                val subtitle = when {
                                    uiState.isTyping -> "CyberBot is typing... ⚡"
                                    conversation.id == "conv_bot" -> "AI Core Active 🤖"
                                    otherUser?.isOnline == true -> "Online"
                                    else -> "Offline"
                                }
                                Text(
                                    text = subtitle,
                                    fontSize = 11.sp,
                                    color = if (uiState.isTyping || otherUser?.isOnline == true || conversation.id == "conv_bot") accentColor else TextMuted
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* Call action */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Call,
                            contentDescription = "Audio Call",
                            tint = accentColor
                        )
                    }
                    IconButton(onClick = { /* Video call action */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Videocam,
                            contentDescription = "Video Call",
                            tint = accentColor
                        )
                    }
                    IconButton(onClick = { /* Info action */ }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More Options",
                            tint = TextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        bottomBar = {
            ChatInputBar(
                text = uiState.inputText,
                onTextChanged = viewModel::onInputTextChanged,
                onSendMessage = viewModel::sendMessage,
                accentColor = accentColor
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading && uiState.messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = accentColor)
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = uiState.messages,
                        key = { it.id }
                    ) { message ->
                        val isSelf = message.senderId == uiState.currentUser?.id
                        val senderUser = conversation?.participants?.find { it.id == message.senderId }

                        MessageBubble(
                            message = message,
                            isSelf = isSelf,
                            sender = senderUser,
                            accentColorHex = conversation?.accentColorHex
                        )
                    }

                    // Typing Indicator Item
                    if (uiState.isTyping) {
                        item(key = "typing_indicator") {
                            AnimatedVisibility(
                                visible = uiState.isTyping,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                TypingIndicatorItem(
                                    conversation = conversation,
                                    accentColor = accentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingIndicatorItem(
    conversation: Conversation?,
    accentColor: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        NeonAvatar(
            name = conversation?.title ?: "Bot",
            neonColorHex = conversation?.accentColorHex,
            size = 32.dp,
            showGlow = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = accentColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .pulsingNeonGlow(
                    color = accentColor,
                    minAlpha = 0.2f,
                    maxAlpha = 0.6f,
                    radius = 8.dp,
                    cornerRadius = 16.dp
                )
                .clip(RoundedCornerShape(16.dp))
                .background(DarkBubbleOther)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${conversation?.title ?: "Contact"} is typing",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "⚡",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    GlowMessagerTheme {
        ChatScreen(
            conversationId = "conv_bot",
            onBackClick = {}
        )
    }
}
