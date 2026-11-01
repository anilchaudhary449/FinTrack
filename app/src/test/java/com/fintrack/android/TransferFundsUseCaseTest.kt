package com.fintrack.android

import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.TransactionRepository
import com.fintrack.android.domain.usecase.TransferFundsUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class TransferFundsUseCaseTest {

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var transferFundsUseCase: TransferFundsUseCase

    @Before
    fun setUp() {
        transactionRepository = mockk(relaxed = true)
        accountRepository = mockk(relaxed = true)
        transferFundsUseCase = TransferFundsUseCase(transactionRepository, accountRepository)
    }

    @Test
    fun `transfer between different accounts generates TRANSFER transaction with correct details`() = runTest {
        val fromAccount = Account(
            id = 1L,
            name = "Checking Bank",
            type = AccountType.BANK_ACCOUNT,
            openingBalance = BigDecimal("10000.00"),
            currentBalance = BigDecimal("10000.00"),
            currency = "NPR"
        )
        val toAccount = Account(
            id = 2L,
            name = "eSewa Wallet",
            type = AccountType.DIGITAL_WALLET,
            openingBalance = BigDecimal("1000.00"),
            currentBalance = BigDecimal("1000.00"),
            currency = "NPR"
        )

        coEvery { accountRepository.getAccountById(1L) } returns fromAccount
        coEvery { accountRepository.getAccountById(2L) } returns toAccount
        coEvery { transactionRepository.addTransaction(any()) } returns 101L

        val resultId = transferFundsUseCase(
            fromAccountId = 1L,
            toAccountId = 2L,
            amount = BigDecimal("2500.00"),
            notes = "Topup eSewa"
        )

        assertEquals(101L, resultId)

        val txSlot = slot<Transaction>()
        coVerify(exactly = 1) { transactionRepository.addTransaction(capture(txSlot)) }

        val captured = txSlot.captured
        assertEquals(TransactionType.TRANSFER, captured.type)
        assertEquals(BigDecimal("2500.00"), captured.amount)
        assertEquals(1L, captured.accountId)
        assertEquals(2L, captured.toAccountId)
        assertEquals("Topup eSewa", captured.notes)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `transfer fails when source and destination accounts are the same`() = runTest {
        transferFundsUseCase(
            fromAccountId = 1L,
            toAccountId = 1L,
            amount = BigDecimal("500.00")
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `transfer fails when amount is zero or negative`() = runTest {
        transferFundsUseCase(
            fromAccountId = 1L,
            toAccountId = 2L,
            amount = BigDecimal("0.00")
        )
    }
}

