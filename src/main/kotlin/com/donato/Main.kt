package com.donato

import com.donato.Utils.getFileFromResources
import com.donato.webhook.*
import java.io.File


fun main() {
    val configuration = Configuration()
    val backoffStrategy = ExponentialBackoffStrategy(configuration)
    val sender = WebhookSender(backoffStrategy)
    val processor = WebhookProcessor(sender)

    getFileFromResources("webhooks.txt").useLines { lines ->
        lines.drop(1).forEach { line ->
            val (url, orderId, name, event) = line.split(", ")
            processor.addToQueue(Webhook(url, orderId.toInt(), name, event))
        }
    }

    processor.processQueue()
}

object Utils {

    fun getFileFromResources(fileName: String): File {
        val file = File(fileName)
        if (file.exists()) {
            return file
        }
        val resourcePath = this::class.java.classLoader.getResource(fileName)?.path
            ?: throw IllegalArgumentException("File not found: $fileName")
        return File(resourcePath)
    }
}