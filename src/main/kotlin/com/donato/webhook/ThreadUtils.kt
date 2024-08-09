package com.donato.webhook

import kotlinx.coroutines.delay

interface ThreadUtils {
    suspend fun wait(timeMillis: Long)
}

class DefaultThreadUtils : ThreadUtils {
    override suspend fun wait(timeMillis: Long) {
        delay(timeMillis)
    }
}

class FakeThreadUtils : ThreadUtils {
    override suspend fun wait(timeMillis: Long) {
        delay(1)
    }
}