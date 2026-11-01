package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.TransactionEntity
import com.fintrack.android.data.local.entity.TransactionWithDetails
import com.fintrack.android.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC, id DESC")
    fun getAllTransactionsWithDetailsFlow(): Flow<List<TransactionWithDetails>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC, id DESC LIMIT :limit")
    fun getRecentTransactionsWithDetailsFlow(limit: Int): Flow<List<TransactionWithDetails>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE transactionDate BETWEEN :startDate AND :endDate ORDER BY transactionDate DESC, id DESC")
    fun getTransactionsBetweenDatesFlow(startDate: Long, endDate: Long): Flow<List<TransactionWithDetails>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE transactionDate BETWEEN :startDate AND :endDate ORDER BY transactionDate DESC, id DESC")
    suspend fun getTransactionsBetweenDatesDirect(startDate: Long, endDate: Long): List<TransactionWithDetails>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE accountId = :accountId OR toAccountId = :accountId ORDER BY transactionDate DESC, id DESC")
    fun getTransactionsForAccountFlow(accountId: Long): Flow<List<TransactionWithDetails>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY transactionDate DESC, id DESC")
    fun getTransactionsForCategoryFlow(categoryId: Long): Flow<List<TransactionWithDetails>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionWithDetailsById(id: Long): TransactionWithDetails?

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionEntityById(id: Long): TransactionEntity?

    @androidx.room.Transaction
    @Query("""
        SELECT * FROM transactions 
        WHERE (notes LIKE '%' || :query || '%' OR amount LIKE '%' || :query || '%')
        ORDER BY transactionDate DESC, id DESC
    """)
    fun searchTransactionsFlow(query: String): Flow<List<TransactionWithDetails>>

    @androidx.room.Transaction
    @Query("""
        SELECT * FROM transactions 
        WHERE (:type IS NULL OR type = :type)
        AND (:accountId IS NULL OR accountId = :accountId OR toAccountId = :accountId)
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        AND (transactionDate BETWEEN :startDate AND :endDate)
        ORDER BY transactionDate DESC, id DESC
    """)
    fun getFilteredTransactionsFlow(
        type: TransactionType?,
        accountId: Long?,
        categoryId: Long?,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getTransactionsCount(): Int
}

