package com.fintrack.android.domain.model

import java.math.BigDecimal

data class RecurringTransaction(
    val id: Long = 0,
    val title: String,
    val type: TransactionType,
    val amount: BigDecimal,
    val categoryId: Long? = null,
    val accountId: Long,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val frequency: RecurrenceFrequency = RecurrenceFrequency.MONTHLY,
    val intervalValue: Int = 1, // e.g. every 1 month, every 2 weeks
    val startDate: Long,
    val endDate: Long? = null,
    val nextDueDate: Long,
    val lastProcessedDate: Long? = null,
    val isActive: Boolean = true,
    val autoCreate: Boolean = true,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

