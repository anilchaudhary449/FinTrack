package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.DebtEntity
import com.fintrack.android.data.local.entity.DebtPaymentEntity
import com.fintrack.android.domain.model.DebtStatus
import com.fintrack.android.domain.model.DebtType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface DebtDao {

    @Query("SELECT * FROM debts ORDER BY status ASC, dueDate ASC")
    fun getAllDebtsFlow(): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debts WHERE type = :type ORDER BY status ASC, dueDate ASC")
    fun getDebtsByTypeFlow(type: DebtType): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debts WHERE id = :id")
    suspend fun getDebtById(id: Long): DebtEntity?

    @Query("SELECT * FROM debt_payments WHERE debtId = :debtId ORDER BY paymentDate DESC")
    fun getPaymentsForDebtFlow(debtId: Long): Flow<List<DebtPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtEntity): Long

    @Update
    suspend fun updateDebt(debt: DebtEntity)

    @Delete
    suspend fun deleteDebt(debt: DebtEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: DebtPaymentEntity): Long

    @Query("UPDATE debts SET outstandingAmount = :newOutstanding, status = :newStatus, updatedAt = :updatedAt WHERE id = :debtId")
    suspend fun updateDebtOutstanding(
        debtId: Long,
        newOutstanding: BigDecimal,
        newStatus: DebtStatus,
        updatedAt: Long = System.currentTimeMillis()
    )
}

