package com.donato.webhook

import kotlinx.coroutines.runBlocking
import com.donato.webhook.ExponentialBackoffStrategy.Companion.DEFAULT_INITIAL_RETRY

class WebhookSender(
    private val backoffStrategy: BackoffStrategy,
    private val networkClient: NetworkClient = HttpNetworkClient(),
    private val threadUtils: ThreadUtils = DefaultThreadUtils(),
    private val logger: Logger = ConsoleLogger()
) {

    fun send(webhook: Webhook): WebhookResult = runBlocking {
        if (!backoffStrategy.isEndpointAllowed(webhook.url)) {
            logger.log("Endpoint ${webhook.url} has failed ${backoffStrategy.maxFailurePerEndpoint()} times. Skipping.")
            return@runBlocking WebhookResult(false)
        }

        var retryCount = DEFAULT_INITIAL_RETRY
        var canExecute = true
        while (canExecute) {
            val result = executeRequest(webhook, retryCount++)
            if (result.success) {
                return@runBlocking result
            }

            try {
                val time = backoffStrategy.getDelay(retryCount)
                logger.log("Webhook ${webhook.url} next waiting $time ms")
                threadUtils.wait(time)
            } catch (e: IllegalArgumentException) {
                logger.log("Endpoint ${webhook.url} has exceed max delay")
                canExecute = false
                retryCount--
            }
        }

        backoffStrategy.incrementFailures(webhook.url)
        logger.log("Webhook failed after $retryCount retries: ${webhook.url}")
        return@runBlocking WebhookResult(false, retryCount)
    }

    private fun executeRequest(webhook: Webhook, retryCount: Int): WebhookResult {
        try {
            val responseCode = networkClient.send(webhook.url, webhook.toJson())

            if (responseCode in networkClient.rangeOfSuccessResponseCode()) {
                logger.log("Webhook sent successfully: ${webhook.url}")
                backoffStrategy.resetFailures(webhook.url)
                return WebhookResult(true, retryCount)
            }
        } catch (e: Exception) {
            logger.log("Error sending webhook: ${e.message}")
        }

        return WebhookResult(false, retryCount)

    }
}