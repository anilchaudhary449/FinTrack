package com.fintrack.android.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

data class ExchangeRateSnapshot(
    val baseCurrency: String,
    val rates: Map<String, BigDecimal>,
    val updatedAtMillis: Long
) {
    fun convert(amount: BigDecimal, fromCurrency: String, toCurrency: String): BigDecimal? {
        val from = fromCurrency.uppercase()
        val to = toCurrency.uppercase()
        if (from == to) return amount

        val fromRate = rates[from] ?: return null
        val toRate = rates[to] ?: return null
        if (fromRate.compareTo(BigDecimal.ZERO) <= 0) return null

        return amount.multiply(toRate)
            .divide(fromRate, 8, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN)
    }
}