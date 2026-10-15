package com.example.glowmessager.ui.chat

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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: MockMessagingRepository
    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MockMessagingRepository(scope = testScope)
        viewModel = ChatViewModel(repository = repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadConversation_loadsDetailsAndMessages() = runTest(testDispatcher) {
        val convId = "conv_bot"
        viewModel.loadConversation(convId)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.conversation)
        assertEquals("CyberBot AI", state.conversation?.title)
        assertTrue(state.messages.isNotEmpty())
    }

    @Test
    fun testInputTextChange_updatesState() = runTest(testDispatcher) {
        viewModel.onInputTextChanged("Hello CyberBot")
        assertEquals("Hello CyberBot", viewModel.uiState.value.inputText)
    }

    @Test
    fun testSendMessage_addsMessageAndClearsInput() = runTest(testDispatcher) {
        val convId = "conv_bot"
        viewModel.loadConversation(convId)

        val initialCount = viewModel.uiState.value.messages.size

        viewModel.onInputTextChanged("Hello test message")
        viewModel.sendMessage()

        val state = viewModel.uiState.value
        assertEquals("", state.inputText)
        assertTrue(state.messages.size > initialCount)
        assertEquals("Hello test message", state.messages.last().text)
    }

    @Test
    fun testMarkAsRead_resetsUnreadCount() = runTest(testDispatcher) {
        val convId = "conv_sam"
        viewModel.loadConversation(convId)
        viewModel.markAsRead()

        val conv = repository.getConversation(convId).first()
        assertEquals(0, conv?.unreadCount)
    }
}
