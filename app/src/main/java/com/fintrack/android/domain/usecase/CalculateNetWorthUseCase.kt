package com.fintrack.android.domain.usecase

import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.DebtRepository
import com.fintrack.android.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import javax.inject.Inject

class CalculateNetWorthUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val debtRepository: DebtRepository,
    private val savingsRepository: SavingsRepository
) {
    operator fun invoke(): Flow<NetWorthSummary> {
        return combine(
            accountRepository.getAllAccounts(),
            debtRepository.getAllDebts(),
            savingsRepository.getAllSavingsGoals()
        ) { accounts, debts, savingsGoals ->

            var cashAndBank = BigDecimal.ZERO
            var digitalWallets = BigDecimal.ZERO
            var creditCardLiabilities = BigDecimal.ZERO
            var accountInvestments = BigDecimal.ZERO

            for (account in accounts) {
                when (account.type) {
                    AccountType.CASH_WALLET,
                    AccountType.BANK_ACCOUNT,
                    AccountType.SAVINGS_ACCOUNT -> {
                        if (account.currentBalance >= BigDecimal.ZERO) {
                            cashAndBank = cashAndBank.add(account.currentBalance)
                        } else {
                            // Overdraft counts as liability
                            creditCardLiabilities = creditCardLiabilities.add(account.currentBalance.abs())
                        }
                    }
                    AccountType.DIGITAL_WALLET -> {
                        digitalWallets = digitalWallets.add(account.currentBalance)
                    }
                    AccountType.CREDIT_CARD -> {
                        // Credit card outstanding balance is liability
                        if (account.currentBalance < BigDecimal.ZERO) {
                            creditCardLiabilities = creditCardLiabilities.add(account.currentBalance.abs())
                        } else {
                            cashAndBank = cashAndBank.add(account.currentBalance)
                        }
                    }
                    AccountType.INVESTMENT -> {
                        accountInvestments = accountInvestments.add(account.currentBalance)
                    }
                    AccountType.OTHER -> {
                        if (account.currentBalance >= BigDecimal.ZERO) {
                            cashAndBank = cashAndBank.add(account.currentBalance)
                        } else {
                            creditCardLiabilities = creditCardLiabilities.add(account.currentBalance.abs())
                        }
                    }
                }
            }

            // Money lent to others (Asset: Owed to me)
            val moneyLentAssets = debts
                .filter { it.type == DebtType.OWED_TO_ME && it.status != DebtStatus.SETTLED }
                .fold(BigDecimal.ZERO) { acc, d -> acc.add(d.outstandingAmount) }

            // Loans & Borrowed money (Liability: Owed by me)
            val loanLiabilities = debts
                .filter { it.type == DebtType.OWED_BY_ME && it.status != DebtStatus.SETTLED }
                .fold(BigDecimal.ZERO) { acc, d -> acc.add(d.outstandingAmount) }

            // Total Assets = Cash + Bank + Digital Wallets + Investments + Money Lent
            val totalAssets = cashAndBank
                .add(digitalWallets)
                .add(accountInvestments)
                .add(moneyLentAssets)

            // Total Liabilities = Loans + Credit Card debt
            val totalLiabilities = loanLiabilities.add(creditCardLiabilities)

            // Net Worth = Total Assets - Total Liabilities
            val netWorth = totalAssets.subtract(totalLiabilities)

            NetWorthSummary(
                totalAssets = totalAssets,
                totalLiabilities = totalLiabilities,
                netWorth = netWorth,
                cashAndBankAssets = cashAndBank,
                digitalWalletAssets = digitalWallets,
                savingsAndInvestments = accountInvestments,
                moneyLentAssets = moneyLentAssets,
                loanLiabilities = loanLiabilities,
                creditCardLiabilities = creditCardLiabilities
            )
        }
    }
}

