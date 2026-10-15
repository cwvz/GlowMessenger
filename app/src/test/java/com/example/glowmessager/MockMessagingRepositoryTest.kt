package com.example.glowmessager

import com.example.glowmessager.data.repository.MockMessagingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MockMessagingRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: MockMessagingRepository

    @Before
    fun setUp() {
        repository = MockMessagingRepository(scope = testScope)
    }

    @Test
    fun testGetConversations_returnsInitialData() = runTest {
        val conversations = repository.getConversations().first()
        assertTrue(conversations.isNotEmpty())
        val botConv = conversations.find { it.id == "conv_bot" }
        assertNotNull(botConv)
        assertEquals("CyberBot AI", botConv?.title)
    }

    @Test
    fun testGetMessages_returnsMessagesForConversation() = runTest {
        val messages = repository.getMessages("conv_bot").first()
        assertTrue(messages.isNotEmpty())
        assertTrue(messages.any { it.text.contains("CyberBot") })
    }

    @Test
    fun testSendMessage_addsMessageAndUpdatesConversation() = runTest(testDispatcher) {
        val initialMessages = repository.getMessages("conv_bot").first()
        val initialCount = initialMessages.size

        val sentMessage = repository.sendMessage("conv_bot", "Testing neon theme")
        assertEquals("Testing neon theme", sentMessage.text)

        val updatedMessages = repository.getMessages("conv_bot").first()
        assertTrue(updatedMessages.size > initialCount)

        val conversation = repository.getConversation("conv_bot").first()
        assertNotNull(conversation)
    }
}
