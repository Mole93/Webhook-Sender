package com.donato.webhook

import java.util.concurrent.ConcurrentLinkedQueue

class WebhookProcessor(private val sender: WebhookSender) {
    private val queue = ConcurrentLinkedQueue<Webhook>()

    fun addToQueue(webhook: Webhook) {
        queue.offer(webhook)
    }

    fun processQueue() {
        while (true) {
            val webhook = queue.poll() ?: break
            sender.send(webhook)
        }
    }
}