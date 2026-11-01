package com.fintrack.android.domain.model

import java.math.BigDecimal

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val categoryId: Long? = null,
    val categoryName: String = "",
    val categoryIcon: String = "",
    val categoryColor: String = "",
    val accountId: Long,
    val accountName: String = "",
    val toAccountId: Long? = null,
    val toAccountName: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val transactionDate: Long = System.currentTimeMillis(),
    val notes: String = "",
    val receiptUri: String? = null,
    val currency: String = "NPR",
    val exchangeRate: BigDecimal = BigDecimal.ONE,
    val recurringTransactionId: Long? = null,
    val debtId: Long? = null,
    val savingsGoalId: Long? = null,
    val isReconciled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

