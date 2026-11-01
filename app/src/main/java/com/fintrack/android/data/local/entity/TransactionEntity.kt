package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.PaymentMethod
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import java.math.BigDecimal

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("accountId"),
        Index("toAccountId"),
        Index("categoryId"),
        Index("transactionDate"),
        Index("type")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val categoryId: Long? = null,
    val accountId: Long,
    val toAccountId: Long? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val transactionDate: Long = System.currentTimeMillis(),
    val notes: String = "",
    val receiptUri: String? = null,
    val currency: String = "NPR",
    val exchangeRate: BigDecimal = BigDecimal.ONE,
    val recurringTransactionId: Long? = null,
    val debtId: Long? = null,
    val savingsGoalId: Long? = null,
    val isReconciled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

