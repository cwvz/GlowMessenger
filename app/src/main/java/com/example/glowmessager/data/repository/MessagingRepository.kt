package com.example.glowmessager.data.repository

import com.example.glowmessager.data.model.Conversation
import com.example.glowmessager.data.model.Message
import com.example.glowmessager.data.model.User
import kotlinx.coroutines.flow.Flow

interface MessagingRepository {
    fun getConversations(): Flow<List<Conversation>>
    fun getConversation(conversationId: String): Flow<Conversation?>
    fun getMessages(conversationId: String): Flow<List<Message>>
    fun getCurrentUser(): User
    suspend fun sendMessage(conversationId: String, text: String, mediaUrl: String? = null): Message
    suspend fun markAsRead(conversationId: String)
    suspend fun triggerBotResponse(conversationId: String, userMessageText: String)
}
