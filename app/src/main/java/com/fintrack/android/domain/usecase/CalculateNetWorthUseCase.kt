package com.fintrack.android.domain.usecase

import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.DebtRepository
import com.fintrack.android.domain.repository.SavingsRepository
import com.fintrack.android.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import javax.inject.Inject

class CalculateNetWorthUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val debtRepository: DebtRepository,
    private val savingsRepository: SavingsRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) {
    operator fun invoke(targetCurrency: String = "NPR"): Flow<NetWorthSummary> {
        return combine(
            accountRepository.getAllAccounts(),
            debtRepository.getAllDebts(),
            savingsRepository.getAllSavingsGoals(),
            exchangeRateRepository.snapshot
        ) { accounts, debts, savingsGoals, rates ->
            fun convert(amount: BigDecimal, sourceCurrency: String): BigDecimal =
                rates?.convert(amount, sourceCurrency, targetCurrency) ?: amount


            var cashAndBank = BigDecimal.ZERO
            var digitalWallets = BigDecimal.ZERO
            var creditCardLiabilities = BigDecimal.ZERO
            var accountInvestments = BigDecimal.ZERO

            for (account in accounts) {
                val balance = convert(account.currentBalance, account.currency)
                when (account.type) {
                    AccountType.CASH_WALLET,
                    AccountType.BANK_ACCOUNT,
                    AccountType.SAVINGS_ACCOUNT -> {
                        if (balance >= BigDecimal.ZERO) {
                            cashAndBank = cashAndBank.add(balance)
                        } else {
                            // Overdraft counts as liability
                            creditCardLiabilities = creditCardLiabilities.add(balance.abs())
                        }
                    }
                    AccountType.DIGITAL_WALLET -> {
                        digitalWallets = digitalWallets.add(balance)
                    }
                    AccountType.CREDIT_CARD -> {
                        // Credit card outstanding balance is liability
                        if (balance < BigDecimal.ZERO) {
                            creditCardLiabilities = creditCardLiabilities.add(balance.abs())
                        } else {
                            cashAndBank = cashAndBank.add(balance)
                        }
                    }
                    AccountType.INVESTMENT -> {
                        accountInvestments = accountInvestments.add(balance)
                    }
                    AccountType.OTHER -> {
                        if (balance >= BigDecimal.ZERO) {
                            cashAndBank = cashAndBank.add(balance)
                        } else {
                            creditCardLiabilities = creditCardLiabilities.add(balance.abs())
                        }
                    }
                }
            }

            // Money lent to others (Asset: Owed to me)
            val moneyLentAssets = debts
                .filter { it.type == DebtType.OWED_TO_ME && it.status != DebtStatus.SETTLED }
                .fold(BigDecimal.ZERO) { acc, debt ->
                    val currency = accounts.firstOrNull { it.id == debt.accountId }?.currency ?: targetCurrency
                    acc.add(convert(debt.outstandingAmount, currency))
                }

            // Loans & Borrowed money (Liability: Owed by me)
            val loanLiabilities = debts
                .filter { it.type == DebtType.OWED_BY_ME && it.status != DebtStatus.SETTLED }
                .fold(BigDecimal.ZERO) { acc, debt ->
                    val currency = accounts.firstOrNull { it.id == debt.accountId }?.currency ?: targetCurrency
                    acc.add(convert(debt.outstandingAmount, currency))
                }

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

