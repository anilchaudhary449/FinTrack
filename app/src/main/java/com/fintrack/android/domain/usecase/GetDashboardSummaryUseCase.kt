package com.fintrack.android.domain.usecase

import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val debtRepository: DebtRepository,
    private val budgetRepository: BudgetRepository,
    private val recurringRepository: RecurringRepository
) {
    operator fun invoke(timeRange: TimeRange, customStart: Long? = null, customEnd: Long? = null): Flow<FinancialSummary> {
        val (startDate, endDate) = resolveDateRange(timeRange, customStart, customEnd)

        return combine(
            accountRepository.getAllAccounts(),
            transactionRepository.getTransactionsBetweenDates(startDate, endDate),
            debtRepository.getAllDebts(),
            recurringRepository.getAllRecurring()
        ) { accounts, transactions, debts, recurring ->
            // 1. Total available balance across active accounts
            val totalBalance = accounts.fold(BigDecimal.ZERO) { acc, account ->
                acc.add(account.currentBalance)
            }

            // 2. Total Income (strictly income transactions, transfers excluded)
            val totalIncome = transactions
                .filter { it.type == TransactionType.INCOME }
                .fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }

            // 3. Total Expense (strictly expense transactions, transfers excluded)
            val totalExpenses = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }

            // 4. Net Savings
            val netSavings = totalIncome.subtract(totalExpenses)

            // 5. Outstanding Debts
            val debtsOwedByMe = debts
                .filter { it.type == DebtType.OWED_BY_ME && it.status != DebtStatus.SETTLED }
                .fold(BigDecimal.ZERO) { acc, d -> acc.add(d.outstandingAmount) }

            val debtsOwedToMe = debts
                .filter { it.type == DebtType.OWED_TO_ME && it.status != DebtStatus.SETTLED }
                .fold(BigDecimal.ZERO) { acc, d -> acc.add(d.outstandingAmount) }

            // 6. Upcoming bills in the next 14 days
            val now = System.currentTimeMillis()
            val twoWeeksAhead = now + (14L * 24 * 60 * 60 * 1000)
            val upcomingBillsCount = recurring.count { it.isActive && it.nextDueDate in now..twoWeeksAhead }

            // 7. Budget utilization percentage
            val budgetUtilization = if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
                totalExpenses.divide(totalIncome, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal(100)).toFloat().coerceIn(0f, 150f)
            } else 0f

            FinancialSummary(
                totalBalance = totalBalance,
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                netSavings = netSavings,
                budgetUtilizationPercentage = budgetUtilization,
                totalDebtsOwedByMe = debtsOwedByMe,
                totalDebtsOwedToMe = debtsOwedToMe,
                upcomingBillsCount = upcomingBillsCount,
                timeRange = timeRange
            )
        }
    }

    private fun resolveDateRange(timeRange: TimeRange, customStart: Long?, customEnd: Long?): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val now = LocalDate.now(zone)

        return when (timeRange) {
            TimeRange.TODAY -> {
                val start = now.atStartOfDay(zone).toInstant().toEpochMilli()
                val end = now.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
                Pair(start, end)
            }
            TimeRange.THIS_WEEK -> {
                val dayOfWeek = now.dayOfWeek.value // 1 (Mon) - 7 (Sun)
                val monday = now.minusDays((dayOfWeek - 1).toLong())
                val start = monday.atStartOfDay(zone).toInstant().toEpochMilli()
                val end = monday.plusDays(7).atStartOfDay(zone).toInstant().toEpochMilli() - 1
                Pair(start, end)
            }
            TimeRange.THIS_MONTH -> {
                val startOfMonth = now.withDayOfMonth(1)
                val start = startOfMonth.atStartOfDay(zone).toInstant().toEpochMilli()
                val nextMonth = startOfMonth.plusMonths(1)
                val end = nextMonth.atStartOfDay(zone).toInstant().toEpochMilli() - 1
                Pair(start, end)
            }
            TimeRange.LAST_MONTH -> {
                val prevMonth = now.minusMonths(1).withDayOfMonth(1)
                val start = prevMonth.atStartOfDay(zone).toInstant().toEpochMilli()
                val thisMonth = prevMonth.plusMonths(1)
                val end = thisMonth.atStartOfDay(zone).toInstant().toEpochMilli() - 1
                Pair(start, end)
            }
            TimeRange.THIS_YEAR -> {
                val startOfYear = now.withDayOfYear(1)
                val start = startOfYear.atStartOfDay(zone).toInstant().toEpochMilli()
                val nextYear = startOfYear.plusYears(1)
                val end = nextYear.atStartOfDay(zone).toInstant().toEpochMilli() - 1
                Pair(start, end)
            }
            TimeRange.CUSTOM -> {
                Pair(customStart ?: 0L, customEnd ?: System.currentTimeMillis())
            }
        }
    }
}

