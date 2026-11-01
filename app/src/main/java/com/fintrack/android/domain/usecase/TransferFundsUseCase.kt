package com.fintrack.android.domain.usecase

import com.fintrack.android.domain.model.PaymentMethod
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

class TransferFundsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        fromAccountId: Long,
        toAccountId: Long,
        amount: BigDecimal,
        notes: String = "",
        date: Long = System.currentTimeMillis()
    ): Long {
        require(fromAccountId != toAccountId) { "Source and destination accounts must be different" }
        require(amount > BigDecimal.ZERO) { "Transfer amount must be greater than zero" }

        val fromAccount = accountRepository.getAccountById(fromAccountId)
            ?: throw IllegalArgumentException("Source account not found")
        val toAccount = accountRepository.getAccountById(toAccountId)
            ?: throw IllegalArgumentException("Destination account not found")

        val transferTx = Transaction(
            type = TransactionType.TRANSFER,
            amount = amount,
            accountId = fromAccountId,
            accountName = fromAccount.name,
            toAccountId = toAccountId,
            toAccountName = toAccount.name,
            paymentMethod = PaymentMethod.BANK_TRANSFER,
            transactionDate = date,
            notes = if (notes.isNotBlank()) notes else "Transfer from ${fromAccount.name} to ${toAccount.name}",
            currency = fromAccount.currency
        )

        return transactionRepository.addTransaction(transferTx)
    }
}

