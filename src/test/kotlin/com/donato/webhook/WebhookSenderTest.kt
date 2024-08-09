package com.donato.webhook

import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertFalse


class WebhookSenderTest {

    private lateinit var sut: WebhookSender
    private lateinit var mockBackoffStrategy: BackoffStrategy
    private lateinit var mockNetworkClient: NetworkClient

    @BeforeEach
    fun setUp() {
        mockBackoffStrategy = spyk(ExponentialBackoffStrategy(Configuration()))
        mockNetworkClient = spyk(HttpNetworkClient())

        sut = WebhookSender(
            backoffStrategy = mockBackoffStrategy,
            networkClient = mockNetworkClient,
            threadUtils = FakeThreadUtils()
        )
    }

    @Test
    fun `test successful webhook send`() {
        val webhook = Webhook("https://example.com", 1, "Test User", "Test Event")

        every { mockNetworkClient.send(any(), any()) } returns 200

        val result = sut.send(webhook)

        assertTrue(result.success)
        assertEquals(1, result.retryCount)
        verify(exactly = 1) { mockNetworkClient.send(any(), any()) }
    }

    @Test
    fun `test webhook send with retries`() {
        val webhook = Webhook("https://example.com", 1, "Test User", "Test Event")

        every { mockNetworkClient.send(any(), any()) } returnsMany listOf(500, 500, 200)

        val result = sut.send(webhook)

        assertTrue(result.success)
        assertEquals(3, result.retryCount)
        verify(exactly = 3) { mockNetworkClient.send(any(), any()) }
        verify(exactly = 2) { mockBackoffStrategy.getDelay(any()) }
    }

    @Test
    fun `test webhook send failure after max retries`() {
        val webhook = Webhook("https://example.com", 1, "Test User", "Test Event")

        every { mockNetworkClient.send(any(), any()) } returns 500

        val result = sut.send(webhook)

        assertFalse(result.success)
        assertEquals(5, result.retryCount)
        verify(exactly = 5) { mockNetworkClient.send(any(), any()) }
        verify(exactly = 5) { mockBackoffStrategy.getDelay(any()) }
    }

    @Test
    fun `test endpoint failure tracking`() {
        val webhook = Webhook("https://example.com", 1, "Test User", "Test Event")

        every { mockNetworkClient.send(any(), any()) } returns 500

        repeat(6) {
            val result = sut.send(webhook)
            assertFalse(result.success)
        }

        verify(exactly = 25) { mockNetworkClient.send(any(), any()) }
    }
}
