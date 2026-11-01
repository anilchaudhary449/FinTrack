package com.fintrack.android.data.repository

import androidx.room.withTransaction
import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.RecurringTransactionEntity
import com.fintrack.android.domain.model.RecurrenceFrequency
import com.fintrack.android.domain.model.RecurringTransaction
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.repository.RecurringRepository
import com.fintrack.android.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val transactionRepository: TransactionRepository
) : RecurringRepository {

    private val recurringDao = database.recurringTransactionDao()

    override fun getAllRecurring(): Flow<List<RecurringTransaction>> {
        return recurringDao.getAllRecurringTransactionsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getRecurringById(id: Long): RecurringTransaction? {
        return recurringDao.getRecurringTransactionById(id)?.toDomain()
    }

    override suspend fun createRecurring(recurring: RecurringTransaction): Long {
        val entity = RecurringTransactionEntity.fromDomain(recurring)
        return recurringDao.insertRecurringTransaction(entity)
    }

    override suspend fun updateRecurring(recurring: RecurringTransaction) {
        val entity = RecurringTransactionEntity.fromDomain(recurring)
        recurringDao.updateRecurringTransaction(entity)
    }

    override suspend fun deleteRecurring(recurring: RecurringTransaction) {
        val entity = RecurringTransactionEntity.fromDomain(recurring)
        recurringDao.deleteRecurringTransaction(entity)
    }

    override suspend fun processDueRecurringTransactions(): Int {
        val now = System.currentTimeMillis()
        val dueList = recurringDao.getDueRecurringTransactions(now)
        var count = 0

        for (recurring in dueList) {
            database.withTransaction {
                // Ensure no duplicate processing for the same timestamp
                if (recurring.lastProcessedDate != null && recurring.lastProcessedDate >= recurring.nextDueDate) {
                    return@withTransaction
                }

                // If autoCreate is true, generate transaction
                if (recurring.autoCreate) {
                    val tx = Transaction(
                        type = recurring.type,
                        amount = recurring.amount,
                        categoryId = recurring.categoryId,
                        accountId = recurring.accountId,
                        paymentMethod = recurring.paymentMethod,
                        transactionDate = recurring.nextDueDate,
                        notes = "Auto-generated: ${recurring.title}. ${recurring.notes}".trim(),
                        recurringTransactionId = recurring.id
                    )
                    transactionRepository.addTransaction(tx)
                }

                // Compute next due date
                val nextDue = computeNextDueDate(recurring.nextDueDate, recurring.frequency, recurring.intervalValue)
                val isStillActive = recurring.endDate == null || nextDue <= recurring.endDate

                recurringDao.updateRecurringTransaction(
                    recurring.copy(
                        lastProcessedDate = now,
                        nextDueDate = nextDue,
                        isActive = isStillActive
                    )
                )
                count++
            }
        }
        return count
    }

    private fun computeNextDueDate(currentDue: Long, frequency: RecurrenceFrequency, interval: Int): Long {
        val zone = ZoneId.systemDefault()
        val date = Instant.ofEpochMilli(currentDue).atZone(zone).toLocalDate()

        val nextDate: LocalDate = when (frequency) {
            RecurrenceFrequency.DAILY -> date.plusDays(interval.toLong())
            RecurrenceFrequency.WEEKLY -> date.plusWeeks(interval.toLong())
            RecurrenceFrequency.MONTHLY -> date.plusMonths(interval.toLong())
            RecurrenceFrequency.YEARLY -> date.plusYears(interval.toLong())
            RecurrenceFrequency.CUSTOM -> date.plusDays(interval.toLong())
        }

        return nextDate.atStartOfDay(zone).toInstant().toEpochMilli()
    }
}

