package com.donato.webhook

interface BackoffStrategy {
    fun getDelay(attempt: Int): Long

    fun isEndpointAllowed(endpoint: String): Boolean

    fun resetFailures(endpoint: String)

    fun incrementFailures(endpoint: String)

    fun maxFailurePerEndpoint(): Int
}

class ExponentialBackoffStrategy(private val configuration: Configuration) : BackoffStrategy {

    private val endpointFailures = mutableMapOf<String, Int>()


    override fun getDelay(attempt: Int): Long {
        val delay = configuration.initialDelay * (1 shl attempt)
        if (delay > configuration.maxDelay) {
            throw IllegalArgumentException("Delay exceeds max delay")
        }

        return delay
    }

    override fun isEndpointAllowed(endpoint: String): Boolean {
        return endpointFailures.getOrDefault(endpoint, DEFAULT_INITIAL_RETRY) <= maxFailurePerEndpoint()
    }

    override fun resetFailures(endpoint: String) {
        endpointFailures[endpoint] = 1
    }

    override fun incrementFailures(endpoint: String) {
        endpointFailures[endpoint] = endpointFailures.getOrDefault(endpoint, DEFAULT_INITIAL_RETRY) + 1
    }

    override fun maxFailurePerEndpoint(): Int {
        return configuration.maxAttemptsPerEndpoint
    }

    companion object {
        const val DEFAULT_INITIAL_RETRY = 1
    }
}
