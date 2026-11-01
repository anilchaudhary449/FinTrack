package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {

    @Query("SELECT * FROM recurring_transactions ORDER BY nextDueDate ASC")
    fun getAllRecurringTransactionsFlow(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextDueDate <= :timestamp")
    suspend fun getDueRecurringTransactions(timestamp: Long): List<RecurringTransactionEntity>

    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    suspend fun getRecurringTransactionById(id: Long): RecurringTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringTransaction(recurring: RecurringTransactionEntity): Long

    @Update
    suspend fun updateRecurringTransaction(recurring: RecurringTransactionEntity)

    @Delete
    suspend fun deleteRecurringTransaction(recurring: RecurringTransactionEntity)

    @Query("UPDATE recurring_transactions SET lastProcessedDate = :lastProcessed, nextDueDate = :nextDue WHERE id = :id")
    suspend fun updateProcessedSchedule(id: Long, lastProcessed: Long, nextDue: Long)
}

