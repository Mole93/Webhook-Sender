package com.donato.webhook

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


class ExponentialBackoffStrategyTest {

    private lateinit var sut: ExponentialBackoffStrategy

    @BeforeEach
    fun setUp() {
        val configuration = Configuration()
        sut = ExponentialBackoffStrategy(configuration)
    }

    @Test
    fun `Given different attempts When getDelay is called Then the delay is exponential increased`() {

        assertEquals(1000, sut.getDelay(0))
        assertEquals(2000, sut.getDelay(1))
        assertEquals(4000, sut.getDelay(2))
        assertEquals(8000, sut.getDelay(3))
        assertEquals(16000, sut.getDelay(4))
        assertEquals(32000, sut.getDelay(5))
    }

    @Test
    fun `Given more attempts then it can handle When getDelay is called Then i get an exception`() {
        assertEquals(32000, sut.getDelay(5))
        assertThrows<IllegalArgumentException> { sut.getDelay(7) }
    }
}