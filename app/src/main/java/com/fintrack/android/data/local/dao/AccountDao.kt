package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts WHERE isArchived = 0 ORDER BY id ASC")
    fun getAllAccountsFlow(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): AccountEntity?

    @Query("SELECT * FROM accounts WHERE isArchived = 0")
    suspend fun getAllAccountsDirect(): List<AccountEntity>

    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun getAccountsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE accounts SET currentBalance = :newBalance, updatedAt = :updatedAt WHERE id = :accountId")
    suspend fun updateAccountBalance(accountId: Long, newBalance: BigDecimal, updatedAt: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteAccount(account: AccountEntity)
}

