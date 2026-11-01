package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.Debt
import com.fintrack.android.domain.model.DebtPayment
import com.fintrack.android.domain.model.DebtStatus
import com.fintrack.android.domain.model.DebtType
import java.math.BigDecimal

@Entity(
    tableName = "debts",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("accountId"),
        Index("type"),
        Index("status")
    ]
)
data class DebtEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val personName: String,
    val type: DebtType,
    val originalAmount: BigDecimal,
    val outstandingAmount: BigDecimal,
    val interestRate: BigDecimal = BigDecimal.ZERO,
    val dueDate: Long? = null,
    val status: DebtStatus = DebtStatus.ACTIVE,
    val notes: String = "",
    val contactNumber: String = "",
    val accountId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Debt = Debt(
        id = id,
        personName = personName,
        type = type,
        originalAmount = originalAmount,
        outstandingAmount = outstandingAmount,
        interestRate = interestRate,
        dueDate = dueDate,
        status = status,
        notes = notes,
        contactNumber = contactNumber,
        accountId = accountId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(d: Debt): DebtEntity = DebtEntity(
            id = d.id,
            personName = d.personName,
            type = d.type,
            originalAmount = d.originalAmount,
            outstandingAmount = d.outstandingAmount,
            interestRate = d.interestRate,
            dueDate = d.dueDate,
            status = d.status,
            notes = d.notes,
            contactNumber = d.contactNumber,
            accountId = d.accountId,
            createdAt = d.createdAt,
            updatedAt = d.updatedAt
        )
    }
}

@Entity(
    tableName = "debt_payments",
    foreignKeys = [
        ForeignKey(
            entity = DebtEntity::class,
            parentColumns = ["id"],
            childColumns = ["debtId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("debtId"),
        Index("accountId")
    ]
)
data class DebtPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val debtId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val paymentDate: Long = System.currentTimeMillis(),
    val notes: String = ""
) {
    fun toDomain(): DebtPayment = DebtPayment(
        id = id,
        debtId = debtId,
        accountId = accountId,
        amount = amount,
        paymentDate = paymentDate,
        notes = notes
    )
}

