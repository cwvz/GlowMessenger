package com.example.glowmessager.ui.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.glowmessager.ui.components.NeonUnreadBadge
import com.example.glowmessager.ui.theme.DarkBackground
import com.example.glowmessager.ui.theme.DarkCardBackground
import com.example.glowmessager.ui.theme.DarkSurface
import com.example.glowmessager.ui.theme.DarkSurfaceVariant
import com.example.glowmessager.ui.theme.GlowMessagerTheme
import com.example.glowmessager.ui.theme.NeonCyan
import com.example.glowmessager.ui.theme.NeonMagenta
import com.example.glowmessager.ui.theme.NeonPurple
import com.example.glowmessager.ui.theme.TextMuted
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.TextSecondary
import com.example.glowmessager.ui.theme.neonBorder
import com.example.glowmessager.ui.theme.pulsingNeonGlow
import com.example.glowmessager.ui.util.ColorUtils
import com.example.glowmessager.ui.util.TimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    onConversationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedConversationId: String? = null,
    viewModel: ConversationListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GlowMessager",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "⚡",
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings / Filter action */ }) {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = "Filter",
                            tint = NeonCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = NeonCyan
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* New message flow */ },
                containerColor = NeonCyan,
                contentColor = DarkBackground,
                shape = CircleShape,
                modifier = Modifier.pulsingNeonGlow(
                    color = NeonCyan,
                    minAlpha = 0.4f,
                    maxAlpha = 0.9f,
                    radius = 12.dp,
                    cornerRadius = 28.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "New Message"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Bar
            SearchBarSection(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged
            )

            // Category Filters
            CategoryFilterSection(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::onFilterSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main Content Area
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            } else if (uiState.conversations.isEmpty()) {
                EmptyConversationsState(query = uiState.searchQuery)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = uiState.conversations,
                        key = { it.id }
                    ) { conversation ->
                        ConversationItem(
                            conversation = conversation,
                            isSelected = conversation.id == selectedConversationId,
                            onClick = {
                                viewModel.markAsRead(conversation.id)
                                onConversationClick(conversation.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBarSection(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        placeholder = {
            Text(
                text = "Search chats, contacts, or CyberBot...",
                color = TextMuted,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = NeonCyan
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear search",
                        tint = TextMuted
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = DarkSurfaceVariant,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        )
    )
}

@Composable
private fun CategoryFilterSection(
    selectedFilter: ConversationFilter,
    onFilterSelected: (ConversationFilter) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        items(ConversationFilter.entries.toTypedArray()) { filter ->
            val isSelected = filter == selectedFilter
            val filterLabel = when (filter) {
                ConversationFilter.ALL -> "All Chats"
                ConversationFilter.UNREAD -> "Unread"
                ConversationFilter.GROUPS -> "Groups"
                ConversationFilter.BOT -> "AI Bot 🤖"
            }
            val accentColor = when (filter) {
                ConversationFilter.ALL -> NeonCyan
                ConversationFilter.UNREAD -> NeonMagenta
                ConversationFilter.GROUPS -> NeonPurple
                ConversationFilter.BOT -> NeonCyan
            }

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filterLabel,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = DarkSurface,
                    labelColor = TextSecondary,
                    selectedContainerColor = accentColor.copy(alpha = 0.2f),
                    selectedLabelColor = accentColor
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = DarkSurfaceVariant,
                    selectedBorderColor = accentColor,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.5.dp
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val accentColor = ColorUtils.parseHexColor(conversation.accentColorHex, NeonCyan)
    val otherUser = conversation.participants.firstOrNull { it.id != "user_me" }
    val isOnline = otherUser?.isOnline == true || conversation.id == "conv_bot"
    val avatarName = conversation.title
    val avatarUrl = otherUser?.avatarUrl

    val cardGlowBorderModifier = when {
        isSelected -> modifier.neonBorder(
            color = accentColor,
            borderWidth = 2.dp,
            cornerRadius = 16.dp,
            glowRadius = 10.dp,
            glowAlpha = 0.6f
        )
        conversation.unreadCount > 0 -> modifier.neonBorder(
            color = accentColor,
            borderWidth = 1.dp,
            cornerRadius = 16.dp,
            glowRadius = 6.dp,
            glowAlpha = 0.3f
        )
        else -> modifier.border(
            width = 1.dp,
            color = DarkSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    val backgroundColor = if (isSelected) {
        accentColor.copy(alpha = 0.12f)
    } else {
        DarkCardBackground
    }

    Box(
        modifier = cardGlowBorderModifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Neon Contact Avatar
            NeonAvatar(
                name = avatarName,
                avatarUrl = avatarUrl,
                neonColorHex = conversation.accentColorHex,
                isOnline = isOnline,
                size = 52.dp,
                showGlow = conversation.unreadCount > 0
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Conversation Content Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (conversation.isPinned) {
                            Icon(
                                imageVector = Icons.Rounded.PushPin,
                                contentDescription = "Pinned",
                                tint = accentColor,
                                modifier = Modifier
                                    .size(14.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                        Text(
                            text = conversation.title,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Timestamp
                    val timeText = TimeUtils.formatConversationTime(conversation.updatedAt)
                    Text(
                        text = timeText,
                        color = if (conversation.unreadCount > 0) accentColor else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Last Message Preview & Unread Badge Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val lastMessageText = conversation.lastMessage?.text ?: "No messages yet"
                    val isHighlighted = conversation.lastMessage?.isGlowHighlighted == true
                    val highlightColor = ColorUtils.parseHexColor(conversation.lastMessage?.glowColorHex, accentColor)

                    Text(
                        text = lastMessageText,
                        color = when {
                            isHighlighted -> highlightColor
                            conversation.unreadCount > 0 -> TextPrimary
                            else -> TextSecondary
                        },
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (conversation.unreadCount > 0 || isHighlighted) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        NeonUnreadBadge(
                            count = conversation.unreadCount,
                            badgeColor = accentColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyConversationsState(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
                    .neonBorder(
                        color = NeonCyan,
                        borderWidth = 1.dp,
                        glowRadius = 10.dp,
                        glowAlpha = 0.4f
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚡",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (query.isBlank()) "No Active Conversations" else "No matching chats found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (query.isBlank()) "Start a new conversation or chat with CyberBot AI!" else "Try searching for another contact or message.",
                fontSize = 14.sp,
                color = TextMuted
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationListScreenPreview() {
    GlowMessagerTheme {
        ConversationListScreen(onConversationClick = {})
    }
}
