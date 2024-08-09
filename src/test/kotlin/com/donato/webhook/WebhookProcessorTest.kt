package com.donato.webhook

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test


class WebhookProcessorTest {

    private lateinit var sender: WebhookSender
    private lateinit var processor: WebhookProcessor

    @BeforeEach
    fun setUp() {
        sender = mockk()
        processor = WebhookProcessor(sender)
    }

    @Test
    fun `test queue processing`() {
        val webhook1 = Webhook("https://example1.com", 1, "User1", "Event1")
        val webhook2 = Webhook("https://example2.com", 2, "User2", "Event2")

        processor.addToQueue(webhook1)
        processor.addToQueue(webhook2)

        every { sender.send(any()) } returns WebhookResult(true)

        processor.processQueue()

        verify { sender.send(webhook1) }
        verify { sender.send(webhook2) }
    }

    @Test
    fun `test queue processing with failures`() {
        val webhook1 = Webhook("https://example1.com", 1, "User1", "Event1")
        val webhook2 = Webhook("https://example2.com", 2, "User2", "Event2")
        val webhook3 = Webhook("https://example3.com", 3, "User3", "Event3")

        processor.addToQueue(webhook1)
        processor.addToQueue(webhook2)
        processor.addToQueue(webhook3)

        every { sender.send(webhook1) } returns WebhookResult(true)
        every { sender.send(webhook2) } returns WebhookResult(false)
        every { sender.send(webhook3) } returns WebhookResult(true)

        processor.processQueue()

        verify { sender.send(webhook1) }
        verify { sender.send(webhook2) }
        verify { sender.send(webhook3) }
    }
}