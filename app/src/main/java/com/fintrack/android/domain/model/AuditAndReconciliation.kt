package com.fintrack.android.domain.model

import java.math.BigDecimal

data class AuditLog(
    val id: Long = 0,
    val entityType: String, // "TRANSACTION", "ACCOUNT", "BUDGET", "DEBT", "SAVINGS"
    val entityId: Long,
    val action: AuditAction,
    val description: String,
    val previousStateJson: String? = null,
    val newStateJson: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ReconciliationRecord(
    val id: Long = 0,
    val accountId: Long,
    val reconciliationDate: Long = System.currentTimeMillis(),
    val appCalculatedBalance: BigDecimal,
    val actualStatementBalance: BigDecimal,
    val discrepancyAmount: BigDecimal,
    val adjustmentTransactionId: Long? = null,
    val notes: String = ""
)

