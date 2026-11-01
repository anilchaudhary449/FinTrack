package com.fintrack.android.data.repository

import androidx.room.withTransaction
import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.data.local.entity.TransactionEntity
import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : TransactionRepository {

    private val transactionDao = database.transactionDao()
    private val accountDao = database.accountDao()
    private val auditDao = database.auditLogDao()

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactionsWithDetailsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactionsWithDetailsFlow(limit).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTransactionsBetweenDates(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsBetweenDatesFlow(startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionsBetweenDatesDirect(startDate: Long, endDate: Long): List<Transaction> {
        return transactionDao.getTransactionsBetweenDatesDirect(startDate, endDate).map { it.toDomain() }
    }

    override fun getTransactionsForAccount(accountId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForAccountFlow(accountId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTransactionsForCategory(categoryId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForCategoryFlow(categoryId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionWithDetailsById(id)?.toDomain()
    }

    override fun searchTransactions(query: String): Flow<List<Transaction>> {
        return transactionDao.searchTransactionsFlow(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getFilteredTransactions(
        type: TransactionType?,
        accountId: Long?,
        categoryId: Long?,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return transactionDao.getFilteredTransactionsFlow(type, accountId, categoryId, startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val entity = TransactionEntity(
                type = transaction.type,
                amount = transaction.amount,
                categoryId = transaction.categoryId,
                accountId = transaction.accountId,
                toAccountId = transaction.toAccountId,
                paymentMethod = transaction.paymentMethod,
                transactionDate = transaction.transactionDate,
                notes = transaction.notes,
                receiptUri = transaction.receiptUri,
                currency = transaction.currency,
                exchangeRate = transaction.exchangeRate,
                recurringTransactionId = transaction.recurringTransactionId,
                debtId = transaction.debtId,
                savingsGoalId = transaction.savingsGoalId,
                isReconciled = transaction.isReconciled,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            val insertedId = transactionDao.insertTransaction(entity)

            // Apply financial impact to account balances
            applyTransactionBalance(entity)

            // Write immutable audit log
            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "TRANSACTION",
                    entityId = insertedId,
                    action = AuditAction.CREATED,
                    description = "Added ${entity.type.name} transaction of ${entity.amount} ${entity.currency}",
                    newStateJson = entity.toString()
                )
            )

            insertedId
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        database.withTransaction {
            val existing = transactionDao.getTransactionEntityById(transaction.id)
                ?: return@withTransaction

            // Revert existing transaction impact
            revertTransactionBalance(existing)

            val updatedEntity = existing.copy(
                type = transaction.type,
                amount = transaction.amount,
                categoryId = transaction.categoryId,
                accountId = transaction.accountId,
                toAccountId = transaction.toAccountId,
                paymentMethod = transaction.paymentMethod,
                transactionDate = transaction.transactionDate,
                notes = transaction.notes,
                receiptUri = transaction.receiptUri,
                currency = transaction.currency,
                exchangeRate = transaction.exchangeRate,
                updatedAt = System.currentTimeMillis()
            )

            transactionDao.updateTransaction(updatedEntity)

            // Apply new transaction impact
            applyTransactionBalance(updatedEntity)

            // Write audit log
            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "TRANSACTION",
                    entityId = transaction.id,
                    action = AuditAction.UPDATED,
                    description = "Updated transaction #${transaction.id}",
                    previousStateJson = existing.toString(),
                    newStateJson = updatedEntity.toString()
                )
            )
        }
    }

    override suspend fun deleteTransaction(id: Long) {
        database.withTransaction {
            val existing = transactionDao.getTransactionEntityById(id)
                ?: return@withTransaction

            // Revert balance impact
            revertTransactionBalance(existing)

            transactionDao.deleteTransaction(existing)

            // Write audit log
            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "TRANSACTION",
                    entityId = id,
                    action = AuditAction.DELETED,
                    description = "Deleted ${existing.type.name} transaction of ${existing.amount} ${existing.currency}",
                    previousStateJson = existing.toString()
                )
            )
        }
    }

    private suspend fun applyTransactionBalance(tx: TransactionEntity) {
        val account = accountDao.getAccountById(tx.accountId) ?: return

        when (tx.type) {
            TransactionType.INCOME,
            TransactionType.DEBT_BORROWED,
            TransactionType.DEBT_REPAYMENT_RECEIVED,
            TransactionType.SAVINGS_WITHDRAWAL -> {
                val newBal = account.currentBalance.add(tx.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }
            TransactionType.EXPENSE,
            TransactionType.DEBT_LENT,
            TransactionType.DEBT_REPAYMENT_MADE,
            TransactionType.SAVINGS_CONTRIBUTION -> {
                val newBal = account.currentBalance.subtract(tx.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }
            TransactionType.TRANSFER -> {
                // Deduct from source
                val sourceNewBal = account.currentBalance.subtract(tx.amount)
                accountDao.updateAccountBalance(account.id, sourceNewBal)

                // Add to destination
                if (tx.toAccountId != null) {
                    val toAccount = accountDao.getAccountById(tx.toAccountId)
                    if (toAccount != null) {
                        val toNewBal = toAccount.currentBalance.add(tx.amount)
                        accountDao.updateAccountBalance(toAccount.id, toNewBal)
                    }
                }
            }
            TransactionType.RECONCILIATION_ADJUSTMENT -> {
                val newBal = account.currentBalance.add(tx.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }
        }
    }

    private suspend fun revertTransactionBalance(tx: TransactionEntity) {
        val account = accountDao.getAccountById(tx.accountId) ?: return

        when (tx.type) {
            TransactionType.INCOME,
            TransactionType.DEBT_BORROWED,
            TransactionType.DEBT_REPAYMENT_RECEIVED,
            TransactionType.SAVINGS_WITHDRAWAL -> {
                val newBal = account.currentBalance.subtract(tx.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }
            TransactionType.EXPENSE,
            TransactionType.DEBT_LENT,
            TransactionType.DEBT_REPAYMENT_MADE,
            TransactionType.SAVINGS_CONTRIBUTION -> {
                val newBal = account.currentBalance.add(tx.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }
            TransactionType.TRANSFER -> {
                // Refund source
                val sourceNewBal = account.currentBalance.add(tx.amount)
                accountDao.updateAccountBalance(account.id, sourceNewBal)

                // Revert destination
                if (tx.toAccountId != null) {
                    val toAccount = accountDao.getAccountById(tx.toAccountId)
                    if (toAccount != null) {
                        val toNewBal = toAccount.currentBalance.subtract(tx.amount)
                        accountDao.updateAccountBalance(toAccount.id, toNewBal)
                    }
                }
            }
            TransactionType.RECONCILIATION_ADJUSTMENT -> {
                val newBal = account.currentBalance.subtract(tx.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }
        }
    }
}

