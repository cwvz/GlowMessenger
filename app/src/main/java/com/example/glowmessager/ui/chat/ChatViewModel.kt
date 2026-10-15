package com.example.glowmessager.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowmessager.data.model.Conversation
import com.example.glowmessager.data.model.Message
import com.example.glowmessager.data.model.User
import com.example.glowmessager.data.repository.MessagingRepository
import com.example.glowmessager.data.repository.MockMessagingRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = true,
    val conversation: Conversation? = null,
    val messages: List<Message> = emptyList(),
    val currentUser: User? = null,
    val inputText: String = "",
    val isSending: Boolean = false,
    val isTyping: Boolean = false
)

class ChatViewModel(
    private val repository: MessagingRepository = MockMessagingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var currentConversationId: String? = null
    private var typingJob: Job? = null

    fun loadConversation(conversationId: String) {
        if (currentConversationId == conversationId) return
        currentConversationId = conversationId

        val currentUser = repository.getCurrentUser()
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            currentUser = currentUser
        )

        // Mark read immediately
        viewModelScope.launch {
            repository.markAsRead(conversationId)
        }

        combine(
            repository.getConversation(conversationId),
            repository.getMessages(conversationId)
        ) { conversation, messages ->
            Pair(conversation, messages)
        }.onEach { (conversation, messages) ->
            val prevMessageCount = _uiState.value.messages.size
            val isNewIncomingMessage = messages.size > prevMessageCount &&
                    messages.lastOrNull()?.senderId != currentUser.id

            if (isNewIncomingMessage) {
                // Cancel typing indicator on receiving new message
                typingJob?.cancel()
                _uiState.value = _uiState.value.copy(isTyping = false)
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                conversation = conversation,
                messages = messages
            )
        }.launchIn(viewModelScope)
    }

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage() {
        val convId = currentConversationId ?: return
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        _uiState.value = _uiState.value.copy(
            inputText = "",
            isSending = true
        )

        viewModelScope.launch {
            try {
                repository.sendMessage(convId, text)
                _uiState.value = _uiState.value.copy(
                    isSending = false,
                    isTyping = true
                )

                // Simulate typing indicator timeout safety check
                typingJob?.cancel()
                typingJob = launch {
                    delay(3000)
                    _uiState.value = _uiState.value.copy(isTyping = false)
                }

                // Ensure thread is marked as read
                repository.markAsRead(convId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSending = false, isTyping = false)
            }
        }
    }

    fun markAsRead() {
        val convId = currentConversationId ?: return
        viewModelScope.launch {
            repository.markAsRead(convId)
        }
    }
}
