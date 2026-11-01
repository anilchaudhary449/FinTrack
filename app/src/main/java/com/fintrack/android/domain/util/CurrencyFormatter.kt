package com.fintrack.android.domain.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {

    fun format(
        amount: BigDecimal?,
        currencyCode: String = "NPR",
        includeSymbol: Boolean = true
    ): String {
        val safeAmount = amount ?: BigDecimal.ZERO
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            decimalSeparator = '.'
            groupingSeparator = ','
        }
        val df = DecimalFormat("#,##0.00", symbols)
        val formattedNumber = df.format(safeAmount)

        if (!includeSymbol) return formattedNumber

        val symbol = getSymbol(currencyCode)
        return when (currencyCode.uppercase()) {
            "NPR" -> "$symbol $formattedNumber"
            "USD" -> "$symbol$formattedNumber"
            "EUR" -> "$symbol$formattedNumber"
            "GBP" -> "$symbol$formattedNumber"
            "INR" -> "$symbol $formattedNumber"
            else -> "$currencyCode $formattedNumber"
        }
    }

    fun getSymbol(currencyCode: String): String {
        return when (currencyCode.uppercase()) {
            "NPR" -> "रु"
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "INR" -> "₹"
            "AUD" -> "A$"
            "CAD" -> "C$"
            "JPY" -> "¥"
            else -> currencyCode
        }
    }
}

