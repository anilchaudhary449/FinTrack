package com.fintrack.android

import com.fintrack.android.domain.model.ExchangeRateSnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.math.BigDecimal

class ExchangeRateSnapshotTest {
    private val rates = ExchangeRateSnapshot(
        baseCurrency = "USD",
        rates = mapOf(
            "USD" to BigDecimal("1"),
            "NPR" to BigDecimal("152"),
            "INR" to BigDecimal("95")
        ),
        updatedAtMillis = 1L
    )

    @Test
    fun `convert from base to target and back`() {
        assertEquals(BigDecimal("15200.00"), rates.convert(BigDecimal("100"), "USD", "NPR"))
        assertEquals(BigDecimal("100.00"), rates.convert(BigDecimal("15200"), "NPR", "USD"))
    }

    @Test
    fun `same currency conversion leaves amount unchanged`() {
        assertEquals(BigDecimal("12.345"), rates.convert(BigDecimal("12.345"), "USD", "USD"))
    }

    @Test
    fun `missing rate does not invent a conversion`() {
        assertNull(rates.convert(BigDecimal("10"), "CAD", "NPR"))
    }
}