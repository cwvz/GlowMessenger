package com.example.glowmessager.ui.conversation

import com.example.glowmessager.data.repository.MockMessagingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConversationListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: MockMessagingRepository
    private lateinit var viewModel: ConversationListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MockMessagingRepository(scope = testScope)
        viewModel = ConversationListViewModel(repository = repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState_loadsConversations() = runTest(testDispatcher) {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.conversations.isNotEmpty())
    }

    @Test
    fun testSearchQuery_filtersConversations() = runTest(testDispatcher) {
        viewModel.onSearchQueryChanged("CyberBot")
        val state = viewModel.uiState.value
        assertEquals("CyberBot", state.searchQuery)
        assertTrue(state.conversations.all { it.title.contains("CyberBot", ignoreCase = true) })
    }

    @Test
    fun testFilterSelected_filtersUnread() = runTest(testDispatcher) {
        viewModel.onFilterSelected(ConversationFilter.UNREAD)
        val state = viewModel.uiState.value
        assertEquals(ConversationFilter.UNREAD, state.selectedFilter)
        assertTrue(state.conversations.all { it.unreadCount > 0 })
    }

    @Test
    fun testMarkAsRead_clearsUnreadCount() = runTest(testDispatcher) {
        val unreadConvId = "conv_sam"
        viewModel.markAsRead(unreadConvId)
        val conv = repository.getConversation(unreadConvId).first()
        assertEquals(0, conv?.unreadCount)
    }
}
