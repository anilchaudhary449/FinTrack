package com.fintrack.android.domain.model

enum class AccountType(val displayName: String) {
    CASH_WALLET("Cash Wallet"),
    BANK_ACCOUNT("Bank Account"),
    DIGITAL_WALLET("Digital Wallet (eSewa / Khalti)"),
    SAVINGS_ACCOUNT("Savings Account"),
    CREDIT_CARD("Credit Card"),
    INVESTMENT("Investment Account"),
    OTHER("Other")
}

