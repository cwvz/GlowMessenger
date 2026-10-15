package com.example.glowmessager.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val title: String,
    val participants: List<User>,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val accentColorHex: String? = null,
    val isPinned: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
