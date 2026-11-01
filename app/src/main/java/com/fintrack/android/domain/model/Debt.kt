package com.fintrack.android.domain.model

import java.math.BigDecimal

data class Debt(
    val id: Long = 0,
    val personName: String,
    val type: DebtType,
    val originalAmount: BigDecimal,
    val outstandingAmount: BigDecimal,
    val interestRate: BigDecimal = BigDecimal.ZERO, // Percentage e.g. 10.5
    val dueDate: Long? = null,
    val status: DebtStatus = DebtStatus.ACTIVE,
    val notes: String = "",
    val contactNumber: String = "",
    val accountId: Long? = null, // Linked account for disbursement
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class DebtPayment(
    val id: Long = 0,
    val debtId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val paymentDate: Long = System.currentTimeMillis(),
    val notes: String = ""
)

