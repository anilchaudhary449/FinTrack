package com.fintrack.android.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.fintrack.android.domain.model.Transaction

data class TransactionWithDetails(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity?,

    @Relation(
        parentColumn = "toAccountId",
        entityColumn = "id"
    )
    val toAccount: AccountEntity?
) {
    fun toDomain(): Transaction = Transaction(
        id = transaction.id,
        type = transaction.type,
        amount = transaction.amount,
        categoryId = transaction.categoryId,
        categoryName = category?.name ?: "",
        categoryIcon = category?.iconName ?: "category_default",
        categoryColor = category?.colorHex ?: "#6B7280",
        accountId = transaction.accountId,
        accountName = account?.name ?: "",
        toAccountId = transaction.toAccountId,
        toAccountName = toAccount?.name ?: "",
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
        createdAt = transaction.createdAt,
        updatedAt = transaction.updatedAt
    )
}

