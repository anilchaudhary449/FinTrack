package com.fintrack.android.domain.model

enum class PaymentMethod(val displayName: String) {
    CASH("Cash"),
    ESEWA("eSewa"),
    KHALTI("Khalti"),
    FONEPAY("Fonepay"),
    BANK_TRANSFER("Bank Transfer"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    CHEQUE("Cheque"),
    OTHER("Other")
}

