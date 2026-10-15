package com.example.glowmessager.ui.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowmessager.data.model.Conversation
import com.example.glowmessager.data.repository.MessagingRepository
import com.example.glowmessager.data.repository.MockMessagingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

enum class ConversationFilter {
    ALL,
    UNREAD,
    GROUPS,
    BOT
}

data class ConversationListUiState(
    val isLoading: Boolean = true,
    val conversations: List<Conversation> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: ConversationFilter = ConversationFilter.ALL
)

class ConversationListViewModel(
    private val repository: MessagingRepository = MockMessagingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationListUiState())
    val uiState: StateFlow<ConversationListUiState> = _uiState.asStateFlow()

    private val searchQueryState = MutableStateFlow("")
    private val selectedFilterState = MutableStateFlow(ConversationFilter.ALL)

    init {
        observeConversations()
    }

    private fun observeConversations() {
        combine(
            repository.getConversations(),
            searchQueryState,
            selectedFilterState
        ) { conversations, query, filter ->
            val filteredList = filterConversations(conversations, query, filter)
            ConversationListUiState(
                isLoading = false,
                conversations = filteredList,
                searchQuery = query,
                selectedFilter = filter
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        searchQueryState.value = query
    }

    fun onFilterSelected(filter: ConversationFilter) {
        selectedFilterState.value = filter
    }

    fun markAsRead(conversationId: String) {
        viewModelScope.launch {
            repository.markAsRead(conversationId)
        }
    }

    private fun filterConversations(
        conversations: List<Conversation>,
        query: String,
        filter: ConversationFilter
    ): List<Conversation> {
        return conversations.filter { conv ->
            // Filter by type / unread
            val matchesFilter = when (filter) {
                ConversationFilter.ALL -> true
                ConversationFilter.UNREAD -> conv.unreadCount > 0
                ConversationFilter.GROUPS -> conv.isGroup
                ConversationFilter.BOT -> conv.id.contains("bot") || conv.title.contains("Bot", ignoreCase = true)
            }

            // Filter by search query
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                conv.title.contains(query, ignoreCase = true) ||
                        conv.lastMessage?.text?.contains(query, ignoreCase = true) == true ||
                        conv.participants.any { it.name.contains(query, ignoreCase = true) }
            }

            matchesFilter && matchesQuery
        }
    }
}
