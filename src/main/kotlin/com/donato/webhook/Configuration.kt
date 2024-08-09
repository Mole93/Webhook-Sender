package com.donato.webhook

data class Configuration(
    val maxAttemptsPerEndpoint: Int = DEFAULT_MAX_ATTEMPTS,
    val initialDelay: Long = DEFAULT_INITIAL_DELAY,
    val maxDelay: Long = DEFAULT_MAX_DELAY
) {
    companion object {
        const val DEFAULT_MAX_ATTEMPTS = 5
        const val DEFAULT_INITIAL_DELAY = 1000L
        const val DEFAULT_MAX_DELAY = 60000L
    }
}
