package com.donato.webhook

import java.net.HttpURLConnection
import java.net.URI

interface NetworkClient {
    fun send(url: String, payload: String): Int

    fun rangeOfSuccessResponseCode(): IntRange
}

class HttpNetworkClient : NetworkClient {
    override fun send(url: String, payload: String): Int {
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.requestMethod = REQUEST_METHOD
        connection.doOutput = true
        connection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON)

        connection.outputStream.use { it.write(payload.toByteArray()) }

        return connection.responseCode
    }

    override fun rangeOfSuccessResponseCode(): IntRange {
        return 200..299
    }

    companion object {
        private const val CONTENT_TYPE = "Content-Type"
        private const val APPLICATION_JSON = "application/json"
        private const val REQUEST_METHOD = "POST"
    }
}