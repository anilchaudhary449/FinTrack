package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.PaymentMethod
import com.fintrack.android.domain.model.RecurrenceFrequency
import com.fintrack.android.domain.model.RecurringTransaction
import com.fintrack.android.domain.model.TransactionType
import java.math.BigDecimal

@Entity(
    tableName = "recurring_transactions",
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
        Index("categoryId"),
        Index("nextDueDate"),
        Index("isActive")
    ]
)
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: TransactionType,
    val amount: BigDecimal,
    val categoryId: Long? = null,
    val accountId: Long,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val frequency: RecurrenceFrequency = RecurrenceFrequency.MONTHLY,
    val intervalValue: Int = 1,
    val startDate: Long,
    val endDate: Long? = null,
    val nextDueDate: Long,
    val lastProcessedDate: Long? = null,
    val isActive: Boolean = true,
    val autoCreate: Boolean = true,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): RecurringTransaction = RecurringTransaction(
        id = id,
        title = title,
        type = type,
        amount = amount,
        categoryId = categoryId,
        accountId = accountId,
        paymentMethod = paymentMethod,
        frequency = frequency,
        intervalValue = intervalValue,
        startDate = startDate,
        endDate = endDate,
        nextDueDate = nextDueDate,
        lastProcessedDate = lastProcessedDate,
        isActive = isActive,
        autoCreate = autoCreate,
        notes = notes,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(r: RecurringTransaction): RecurringTransactionEntity = RecurringTransactionEntity(
            id = r.id,
            title = r.title,
            type = r.type,
            amount = r.amount,
            categoryId = r.categoryId,
            accountId = r.accountId,
            paymentMethod = r.paymentMethod,
            frequency = r.frequency,
            intervalValue = r.intervalValue,
            startDate = r.startDate,
            endDate = r.endDate,
            nextDueDate = r.nextDueDate,
            lastProcessedDate = r.lastProcessedDate,
            isActive = r.isActive,
            autoCreate = r.autoCreate,
            notes = r.notes,
            createdAt = r.createdAt
        )
    }
}

