package com.fintrack.android.data.repository

import androidx.room.withTransaction
import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.data.local.entity.ReconciliationRecordEntity
import com.fintrack.android.data.local.entity.TransactionEntity
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.model.AuditLog
import com.fintrack.android.domain.model.ReconciliationRecord
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.AuditRepository
import com.fintrack.android.domain.repository.ReconciliationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuditRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : AuditRepository {

    private val auditDao = database.auditLogDao()

    override fun getRecentAuditLogs(limit: Int): Flow<List<AuditLog>> {
        return auditDao.getRecentAuditLogsFlow(limit).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAuditLogsForEntity(entityType: String, entityId: Long): Flow<List<AuditLog>> {
        return auditDao.getAuditLogsForEntityFlow(entityType, entityId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun recordAudit(log: AuditLog): Long {
        val entity = AuditLogEntity.fromDomain(log)
        return auditDao.insertAuditLog(entity)
    }
}

@Singleton
class ReconciliationRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : ReconciliationRepository {

    private val reconciliationDao = database.reconciliationDao()
    private val accountDao = database.accountDao()
    private val transactionDao = database.transactionDao()
    private val auditDao = database.auditLogDao()

    override fun getReconciliationsForAccount(accountId: Long): Flow<List<ReconciliationRecord>> {
        return reconciliationDao.getReconciliationsForAccountFlow(accountId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun reconcileAccount(
        accountId: Long,
        actualBalance: BigDecimal,
        notes: String
    ): Long {
        return database.withTransaction {
            val account = accountDao.getAccountById(accountId)
                ?: throw IllegalArgumentException("Account not found with ID $accountId")

            val appBalance = account.currentBalance
            val discrepancy = actualBalance.subtract(appBalance)
            var adjustmentTxId: Long? = null

            if (discrepancy.compareTo(BigDecimal.ZERO) != 0) {
                // Create an adjustment transaction to keep ledger consistent
                val adjustmentTx = TransactionEntity(
                    type = TransactionType.RECONCILIATION_ADJUSTMENT,
                    amount = discrepancy,
                    accountId = accountId,
                    transactionDate = System.currentTimeMillis(),
                    notes = "Reconciliation adjustment: $notes".trim(),
                    isReconciled = true
                )
                adjustmentTxId = transactionDao.insertTransaction(adjustmentTx)

                // Update account to match actual verified balance
                accountDao.updateAccountBalance(accountId, actualBalance)
            }

            val record = ReconciliationRecordEntity(
                accountId = accountId,
                reconciliationDate = System.currentTimeMillis(),
                appCalculatedBalance = appBalance,
                actualStatementBalance = actualBalance,
                discrepancyAmount = discrepancy,
                adjustmentTransactionId = adjustmentTxId,
                notes = notes
            )
            val recordId = reconciliationDao.insertReconciliationRecord(record)

            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "ACCOUNT_RECONCILIATION",
                    entityId = accountId,
                    action = AuditAction.RECONCILED,
                    description = "Reconciled '${account.name}'. Discrepancy: $discrepancy. Actual balance: $actualBalance."
                )
            )

            recordId
        }
    }
}

