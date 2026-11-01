package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.model.AuditLog
import com.fintrack.android.domain.model.ReconciliationRecord
import java.math.BigDecimal

@Entity(
    tableName = "audit_logs",
    indices = [
        Index("entityType", "entityId"),
        Index("timestamp")
    ]
)
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entityType: String,
    val entityId: Long,
    val action: AuditAction,
    val description: String,
    val previousStateJson: String? = null,
    val newStateJson: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toDomain(): AuditLog = AuditLog(
        id = id,
        entityType = entityType,
        entityId = entityId,
        action = action,
        description = description,
        previousStateJson = previousStateJson,
        newStateJson = newStateJson,
        timestamp = timestamp
    )

    companion object {
        fun fromDomain(a: AuditLog): AuditLogEntity = AuditLogEntity(
            id = a.id,
            entityType = a.entityType,
            entityId = a.entityId,
            action = a.action,
            description = a.description,
            previousStateJson = a.previousStateJson,
            newStateJson = a.newStateJson,
            timestamp = a.timestamp
        )
    }
}

@Entity(
    tableName = "reconciliation_records",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("accountId"),
        Index("reconciliationDate")
    ]
)
data class ReconciliationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val accountId: Long,
    val reconciliationDate: Long = System.currentTimeMillis(),
    val appCalculatedBalance: BigDecimal,
    val actualStatementBalance: BigDecimal,
    val discrepancyAmount: BigDecimal,
    val adjustmentTransactionId: Long? = null,
    val notes: String = ""
) {
    fun toDomain(): ReconciliationRecord = ReconciliationRecord(
        id = id,
        accountId = accountId,
        reconciliationDate = reconciliationDate,
        appCalculatedBalance = appCalculatedBalance,
        actualStatementBalance = actualStatementBalance,
        discrepancyAmount = discrepancyAmount,
        adjustmentTransactionId = adjustmentTransactionId,
        notes = notes
    )

    companion object {
        fun fromDomain(r: ReconciliationRecord): ReconciliationRecordEntity = ReconciliationRecordEntity(
            id = r.id,
            accountId = r.accountId,
            reconciliationDate = r.reconciliationDate,
            appCalculatedBalance = r.appCalculatedBalance,
            actualStatementBalance = r.actualStatementBalance,
            discrepancyAmount = r.discrepancyAmount,
            adjustmentTransactionId = r.adjustmentTransactionId,
            notes = r.notes
        )
    }
}

