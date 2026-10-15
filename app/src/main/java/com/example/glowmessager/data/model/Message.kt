package com.example.glowmessager.data.model

import kotlinx.serialization.Serializable

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

enum class MessageType {
    TEXT,
    IMAGE,
    VOICE,
    SYSTEM
}

@Serializable
data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT,
    val type: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val isGlowHighlighted: Boolean = false,
    val glowColorHex: String? = null
)
