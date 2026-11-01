package com.fintrack.android.data.repository

import androidx.room.withTransaction
import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.data.local.entity.DebtEntity
import com.fintrack.android.data.local.entity.DebtPaymentEntity
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.model.Debt
import com.fintrack.android.domain.model.DebtPayment
import com.fintrack.android.domain.model.DebtStatus
import com.fintrack.android.domain.model.DebtType
import com.fintrack.android.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : DebtRepository {

    private val debtDao = database.debtDao()
    private val accountDao = database.accountDao()
    private val auditDao = database.auditLogDao()

    override fun getAllDebts(): Flow<List<Debt>> {
        return debtDao.getAllDebtsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getDebtsByType(type: DebtType): Flow<List<Debt>> {
        return debtDao.getDebtsByTypeFlow(type).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getDebtById(id: Long): Debt? {
        return debtDao.getDebtById(id)?.toDomain()
    }

    override fun getPaymentsForDebt(debtId: Long): Flow<List<DebtPayment>> {
        return debtDao.getPaymentsForDebtFlow(debtId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun createDebt(debt: Debt): Long {
        return database.withTransaction {
            val entity = DebtEntity.fromDomain(debt)
            val id = debtDao.insertDebt(entity)

            // Adjust linked account balance if selected
            if (debt.accountId != null) {
                val account = accountDao.getAccountById(debt.accountId)
                if (account != null) {
                    val newBal = when (debt.type) {
                        DebtType.OWED_BY_ME -> account.currentBalance.add(debt.originalAmount)
                        DebtType.OWED_TO_ME -> account.currentBalance.subtract(debt.originalAmount)
                    }
                    accountDao.updateAccountBalance(account.id, newBal)
                }
            }

            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "DEBT",
                    entityId = id,
                    action = AuditAction.CREATED,
                    description = "Created ${debt.type.name} record for '${debt.personName}' with amount ${debt.originalAmount}"
                )
            )

            id
        }
    }

    override suspend fun updateDebt(debt: Debt) {
        val entity = DebtEntity.fromDomain(debt)
        debtDao.updateDebt(entity)
    }

    override suspend fun deleteDebt(debt: Debt) {
        val entity = DebtEntity.fromDomain(debt)
        debtDao.deleteDebt(entity)
    }

    override suspend fun recordDebtPayment(payment: DebtPayment): Long {
        return database.withTransaction {
            val debt = debtDao.getDebtById(payment.debtId)
                ?: throw IllegalArgumentException("Debt not found with ID ${payment.debtId}")

            val paymentEntity = DebtPaymentEntity(
                debtId = payment.debtId,
                accountId = payment.accountId,
                amount = payment.amount,
                paymentDate = payment.paymentDate,
                notes = payment.notes
            )
            val insertedId = debtDao.insertPayment(paymentEntity)

            val newOutstanding = (debt.outstandingAmount - payment.amount).max(BigDecimal.ZERO)
            val newStatus = if (newOutstanding.compareTo(BigDecimal.ZERO) == 0) {
                DebtStatus.SETTLED
            } else {
                DebtStatus.PARTIALLY_PAID
            }

            debtDao.updateDebtOutstanding(debt.id, newOutstanding, newStatus)

            // Adjust account balance accordingly
            val account = accountDao.getAccountById(payment.accountId)
            if (account != null) {
                val newBal = when (debt.type) {
                    DebtType.OWED_BY_ME -> account.currentBalance.subtract(payment.amount) // I paid money out
                    DebtType.OWED_TO_ME -> account.currentBalance.add(payment.amount)      // Someone paid me back
                }
                accountDao.updateAccountBalance(account.id, newBal)
            }

            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "DEBT_PAYMENT",
                    entityId = insertedId,
                    action = AuditAction.CREATED,
                    description = "Recorded debt payment of ${payment.amount} for '${debt.personName}'. Remaining: $newOutstanding"
                )
            )

            insertedId
        }
    }
}

