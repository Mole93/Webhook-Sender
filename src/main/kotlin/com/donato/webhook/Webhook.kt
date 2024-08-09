package com.donato.webhook

data class Webhook(
    val url: String,
    val orderId: Int,
    val name: String,
    val event: String
) {
    fun toJson(): String {
        return """ {
                        "orderId": ${orderId},
                        "name": "${name}",
                        "event": "${event}"
                    }
                """.trimIndent()
    }
}

data class WebhookResult(
    val success: Boolean,
    val retryCount: Int = 0,
    val lastAttempt: Long = System.currentTimeMillis()
)