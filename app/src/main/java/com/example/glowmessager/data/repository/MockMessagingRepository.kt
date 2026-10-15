package com.example.glowmessager.data.repository

import com.example.glowmessager.data.model.Conversation
import com.example.glowmessager.data.model.Message
import com.example.glowmessager.data.model.MessageStatus
import com.example.glowmessager.data.model.MessageType
import com.example.glowmessager.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class MockMessagingRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : MessagingRepository {

    private val currentUser = User(
        id = "user_me",
        name = "Alex Mercer",
        avatarUrl = null,
        isOnline = true,
        statusMessage = "Glowing in the Matrix ⚡",
        neonColorHex = "#00F0FF"
    )

    private val botUser = User(
        id = "user_bot",
        name = "CyberBot AI",
        avatarUrl = null,
        isOnline = true,
        statusMessage = "Neon AI Core v2.4 Active 🤖",
        neonColorHex = "#00F0FF"
    )

    private val ariaUser = User(
        id = "user_aria",
        name = "Aria Vance",
        avatarUrl = null,
        isOnline = true,
        statusMessage = "Synthwave & Code 🎵",
        neonColorHex = "#FF007F"
    )

    private val samUser = User(
        id = "user_sam",
        name = "Sam Flynn",
        avatarUrl = null,
        isOnline = false,
        statusMessage = "Grid Explorer",
        neonColorHex = "#00FF66"
    )

    private val novaUser = User(
        id = "user_nova",
        name = "Nova Sterling",
        avatarUrl = null,
        isOnline = true,
        statusMessage = "Neon UI Specialist ✨",
        neonColorHex = "#9D00FF"
    )

    private val initialMessages = mutableMapOf<String, List<Message>>(
        "conv_bot" to listOf(
            Message(
                id = "m_bot_1",
                conversationId = "conv_bot",
                senderId = "user_bot",
                text = "System initialized! Welcome to GlowMessager. ⚡",
                timestamp = System.currentTimeMillis() - 3600000 * 2,
                status = MessageStatus.READ,
                type = MessageType.SYSTEM
            ),
            Message(
                id = "m_bot_2",
                conversationId = "conv_bot",
                senderId = "user_bot",
                text = "I am CyberBot. Type 'glow', 'theme', or ask me anything for a instant response!",
                timestamp = System.currentTimeMillis() - 3600000,
                status = MessageStatus.READ,
                type = MessageType.TEXT,
                isGlowHighlighted = true,
                glowColorHex = "#00F0FF"
            )
        ),
        "conv_aria" to listOf(
            Message(
                id = "m_aria_1",
                conversationId = "conv_aria",
                senderId = "user_me",
                text = "Hey Aria, did you check the new neon accents?",
                timestamp = System.currentTimeMillis() - 1800000,
                status = MessageStatus.READ
            ),
            Message(
                id = "m_aria_2",
                conversationId = "conv_aria",
                senderId = "user_aria",
                text = "Yes! The cyan and magenta contrast looks stunning! 🔥",
                timestamp = System.currentTimeMillis() - 1200000,
                status = MessageStatus.READ
            ),
            Message(
                id = "m_aria_3",
                conversationId = "conv_aria",
                senderId = "user_aria",
                text = "Meeting at the neon rooftop lounge tonight at 9 PM? 🍸",
                timestamp = System.currentTimeMillis() - 600000,
                status = MessageStatus.READ,
                isGlowHighlighted = true,
                glowColorHex = "#FF007F"
            )
        ),
        "conv_sam" to listOf(
            Message(
                id = "m_sam_1",
                conversationId = "conv_sam",
                senderId = "user_sam",
                text = "Check out this sick custom neon green layout!",
                timestamp = System.currentTimeMillis() - 7200000,
                status = MessageStatus.DELIVERED,
                isGlowHighlighted = true,
                glowColorHex = "#00FF66"
            )
        ),
        "conv_group" to listOf(
            Message(
                id = "m_grp_1",
                conversationId = "conv_group",
                senderId = "user_aria",
                text = "Team, let's finalize the Dark Theme presentation.",
                timestamp = System.currentTimeMillis() - 14400000,
                status = MessageStatus.READ
            ),
            Message(
                id = "m_grp_2",
                conversationId = "conv_group",
                senderId = "user_nova",
                text = "The glowing border modifiers and pulse badges look incredible on AMOLED screens!",
                timestamp = System.currentTimeMillis() - 7200000,
                status = MessageStatus.READ,
                isGlowHighlighted = true,
                glowColorHex = "#9D00FF"
            )
        )
    )

    private val conversationsState = MutableStateFlow<List<Conversation>>(
        listOf(
            Conversation(
                id = "conv_bot",
                title = "CyberBot AI",
                participants = listOf(currentUser, botUser),
                lastMessage = initialMessages["conv_bot"]?.lastOrNull(),
                unreadCount = 0,
                isGroup = false,
                accentColorHex = "#00F0FF",
                isPinned = true,
                updatedAt = System.currentTimeMillis() - 3600000
            ),
            Conversation(
                id = "conv_aria",
                title = "Aria Vance",
                participants = listOf(currentUser, ariaUser),
                lastMessage = initialMessages["conv_aria"]?.lastOrNull(),
                unreadCount = 0,
                isGroup = false,
                accentColorHex = "#FF007F",
                isPinned = true,
                updatedAt = System.currentTimeMillis() - 600000
            ),
            Conversation(
                id = "conv_sam",
                title = "Sam Flynn",
                participants = listOf(currentUser, samUser),
                lastMessage = initialMessages["conv_sam"]?.lastOrNull(),
                unreadCount = 1,
                isGroup = false,
                accentColorHex = "#00FF66",
                isPinned = false,
                updatedAt = System.currentTimeMillis() - 7200000
            ),
            Conversation(
                id = "conv_group",
                title = "Neon Syndicate",
                participants = listOf(currentUser, ariaUser, samUser, novaUser),
                lastMessage = initialMessages["conv_group"]?.lastOrNull(),
                unreadCount = 0,
                isGroup = true,
                accentColorHex = "#9D00FF",
                isPinned = false,
                updatedAt = System.currentTimeMillis() - 7200000
            )
        )
    )

    private val messagesMapState = MutableStateFlow<Map<String, List<Message>>>(initialMessages)

    override fun getConversations(): Flow<List<Conversation>> {
        return conversationsState.asStateFlow()
    }

    override fun getConversation(conversationId: String): Flow<Conversation?> {
        return conversationsState.map { list -> list.find { it.id == conversationId } }
    }

    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return messagesMapState.map { map -> map[conversationId] ?: emptyList() }
    }

    override fun getCurrentUser(): User = currentUser

    override suspend fun sendMessage(
        conversationId: String,
        text: String,
        mediaUrl: String?
    ): Message {
        val messageId = "msg_${UUID.randomUUID().toString().take(8)}"
        val newMessage = Message(
            id = messageId,
            conversationId = conversationId,
            senderId = currentUser.id,
            text = text,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.SENT,
            type = if (mediaUrl != null) MessageType.IMAGE else MessageType.TEXT,
            mediaUrl = mediaUrl
        )

        // Update messages
        val currentMap = messagesMapState.value.toMutableMap()
        val currentList = (currentMap[conversationId] ?: emptyList()).toMutableList()
        currentList.add(newMessage)
        currentMap[conversationId] = currentList
        messagesMapState.value = currentMap

        // Update conversation lastMessage
        updateConversationLastMessage(conversationId, newMessage)

        // Trigger auto-reply in background
        scope.launch {
            delay(1000) // Simulate transmission delay
            markUserMessageDelivered(conversationId, messageId)
            delay(1200) // Simulate typing delay
            triggerBotResponse(conversationId, text)
        }

        return newMessage
    }

    override suspend fun markAsRead(conversationId: String) {
        val currentConversations = conversationsState.value.toMutableList()
        val index = currentConversations.indexOfFirst { it.id == conversationId }
        if (index != -1) {
            val conv = currentConversations[index]
            currentConversations[index] = conv.copy(unreadCount = 0)
            conversationsState.value = currentConversations
        }
    }

    override suspend fun triggerBotResponse(conversationId: String, userMessageText: String) {
        val conv = conversationsState.value.find { it.id == conversationId } ?: return

        // Pick responder user
        val responder = if (conv.id == "conv_bot") {
            botUser
        } else {
            conv.participants.firstOrNull { it.id != currentUser.id } ?: botUser
        }

        val replyText = generateBotReplyText(responder.name, userMessageText, conv.isGroup)
        val replyId = "reply_${UUID.randomUUID().toString().take(8)}"

        val replyMessage = Message(
            id = replyId,
            conversationId = conversationId,
            senderId = responder.id,
            text = replyText,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.READ,
            type = MessageType.TEXT,
            isGlowHighlighted = replyText.contains("⚡") || replyText.contains("✨") || replyText.contains("🔥"),
            glowColorHex = responder.neonColorHex
        )

        // Append reply
        val currentMap = messagesMapState.value.toMutableMap()
        val currentList = (currentMap[conversationId] ?: emptyList()).toMutableList()
        currentList.add(replyMessage)
        currentMap[conversationId] = currentList
        messagesMapState.value = currentMap

        // Update conversation
        val isCurrentConvViewing = false // If not focused, increment unread
        val updatedConversations = conversationsState.value.toMutableList()
        val convIndex = updatedConversations.indexOfFirst { it.id == conversationId }
        if (convIndex != -1) {
            val oldConv = updatedConversations[convIndex]
            updatedConversations[convIndex] = oldConv.copy(
                lastMessage = replyMessage,
                unreadCount = oldConv.unreadCount + 1,
                updatedAt = replyMessage.timestamp
            )
            conversationsState.value = updatedConversations.sortedByDescending { it.updatedAt }
        }
    }

    private fun markUserMessageDelivered(conversationId: String, messageId: String) {
        val currentMap = messagesMapState.value.toMutableMap()
        val currentList = (currentMap[conversationId] ?: emptyList()).toMutableList()
        val index = currentList.indexOfFirst { it.id == messageId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(status = MessageStatus.READ)
            currentMap[conversationId] = currentList
            messagesMapState.value = currentMap
        }
    }

    private fun updateConversationLastMessage(conversationId: String, message: Message) {
        val updatedConversations = conversationsState.value.toMutableList()
        val index = updatedConversations.indexOfFirst { it.id == conversationId }
        if (index != -1) {
            val oldConv = updatedConversations[index]
            updatedConversations[index] = oldConv.copy(
                lastMessage = message,
                updatedAt = message.timestamp
            )
            conversationsState.value = updatedConversations.sortedByDescending { it.updatedAt }
        }
    }

    private fun generateBotReplyText(senderName: String, prompt: String, isGroup: Boolean): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
                "Greetings from the Cyber Grid, Alex! ⚡ How is your neon setup going?"

            lower.contains("glow") || lower.contains("neon") || lower.contains("theme") ->
                "The Glowing Dark Theme is operating at 100% luminous efficiency! Cyan, Magenta, and Green accents are active. ✨"

            lower.contains("bot") || lower.contains("ai") || lower.contains("help") ->
                "I am CyberBot v2.4, your AI companion. I can process grid messages, synthesize responses, and keep the vibes glowing. 🤖"

            lower.contains("meeting") || lower.contains("tonight") || lower.contains("time") ->
                "Count me in! I'll have the glowing cyber cocktails ready by 9 PM sharp. 🍸"

            lower.contains("code") || lower.contains("ui") || lower.contains("compose") ->
                "Jetpack Compose + Neon Glow Modifiers = absolute perfection! 🔥"

            else -> {
                val genericReplies = listOf(
                    "Signal received loud and clear over the encrypted dark channel! ⚡",
                    "Synthesizing packet... Neon protocol verified! ✨",
                    "Totally agree! Catch you on the cyberpunk grid later.",
                    "Transmitting high-voltage energy to your terminal... 🚀",
                    "Copy that! Let's keep the conversation glowing."
                )
                genericReplies.random()
            }
        }
    }
}
