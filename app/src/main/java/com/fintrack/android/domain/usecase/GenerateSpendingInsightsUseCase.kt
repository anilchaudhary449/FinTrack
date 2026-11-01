package com.fintrack.android.domain.usecase

import com.fintrack.android.domain.model.InsightPriority
import com.fintrack.android.domain.model.SpendingInsight
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.BudgetRepository
import com.fintrack.android.domain.repository.TransactionRepository
import com.fintrack.android.domain.util.CurrencyFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GenerateSpendingInsightsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<List<SpendingInsight>> {
        val zone = ZoneId.systemDefault()
        val now = LocalDate.now(zone)

        val currentMonthStart = now.withDayOfMonth(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val prevMonthStart = now.minusMonths(1).withDayOfMonth(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val prevMonthEnd = currentMonthStart - 1

        return combine(
            transactionRepository.getTransactionsBetweenDates(currentMonthStart, System.currentTimeMillis()),
            transactionRepository.getTransactionsBetweenDates(prevMonthStart, prevMonthEnd),
            budgetRepository.getBudgetsForMonth(now.monthValue, now.year)
        ) { currentTxs, prevTxs, budgets ->
            val insights = mutableListOf<SpendingInsight>()

            val currentExpenses = currentTxs.filter { it.type == TransactionType.EXPENSE }
            val prevExpenses = prevTxs.filter { it.type == TransactionType.EXPENSE }

            val totalCurrentExpense = currentExpenses.fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }
            val totalPrevExpense = prevExpenses.fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }

            // 1. Highest spending category
            val categoryTotals = currentExpenses.groupBy { it.categoryName.ifEmpty { "General" } }
                .mapValues { entry -> entry.value.fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) } }

            val highestCategory = categoryTotals.maxByOrNull { it.value }
            if (highestCategory != null && totalCurrentExpense > BigDecimal.ZERO) {
                val percentage = highestCategory.value
                    .divide(totalCurrentExpense, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal(100)).toInt()

                insights.add(
                    SpendingInsight(
                        id = "highest_cat",
                        title = "Highest Spending: ${highestCategory.key}",
                        message = "${highestCategory.key} accounts for $percentage% (${CurrencyFormatter.format(highestCategory.value)}) of your expenses this month.",
                        priority = if (percentage > 40) InsightPriority.HIGH else InsightPriority.MEDIUM,
                        categoryName = highestCategory.key,
                        diffPercentage = percentage.toFloat()
                    )
                )
            }

            // 2. Monthly spending comparison
            if (totalPrevExpense > BigDecimal.ZERO) {
                val diff = totalCurrentExpense.subtract(totalPrevExpense)
                val diffPercent = diff.divide(totalPrevExpense, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal(100)).toInt()

                if (diffPercent > 15) {
                    insights.add(
                        SpendingInsight(
                            id = "monthly_increase",
                            title = "Spending Increased by $diffPercent%",
                            message = "You have spent $diffPercent% more this month compared to the same period last month.",
                            priority = InsightPriority.HIGH,
                            diffPercentage = diffPercent.toFloat()
                        )
                    )
                } else if (diffPercent < -10) {
                    insights.add(
                        SpendingInsight(
                            id = "monthly_decrease",
                            title = "Great Job! Spending is Down by ${diffPercent.times(-1)}%",
                            message = "You have managed to reduce spending by ${diffPercent.times(-1)}% compared to last month.",
                            priority = InsightPriority.LOW,
                            diffPercentage = diffPercent.toFloat()
                        )
                    )
                }
            }

            // 3. Average daily expense
            val dayOfMonth = now.dayOfMonth.coerceAtLeast(1)
            if (totalCurrentExpense > BigDecimal.ZERO) {
                val dailyAvg = totalCurrentExpense.divide(BigDecimal(dayOfMonth), 2, RoundingMode.HALF_EVEN)
                insights.add(
                    SpendingInsight(
                        id = "daily_average",
                        title = "Daily Average Spending",
                        message = "You are spending an average of ${CurrencyFormatter.format(dailyAvg)} per day this month.",
                        priority = InsightPriority.LOW
                    )
                )
            }

            // 4. Budget warnings
            for (budget in budgets) {
                val catSpent = if (budget.categoryId != null) {
                    currentExpenses.filter { it.categoryId == budget.categoryId }
                        .fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }
                } else {
                    totalCurrentExpense
                }

                if (budget.amountLimit > BigDecimal.ZERO) {
                    val percent = catSpent.divide(budget.amountLimit, 4, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal(100)).toInt()

                    if (percent >= 100) {
                        insights.add(
                            SpendingInsight(
                                id = "budget_exceeded_${budget.id}",
                                title = "Budget Exceeded: ${budget.name}",
                                message = "You have exceeded your limit by spending ${CurrencyFormatter.format(catSpent)} of ${CurrencyFormatter.format(budget.amountLimit)} ($percent%).",
                                priority = InsightPriority.HIGH,
                                categoryName = budget.categoryName
                            )
                        )
                    } else if (percent >= budget.warningThresholdPercentage) {
                        insights.add(
                            SpendingInsight(
                                id = "budget_warn_${budget.id}",
                                title = "Budget Warning: ${budget.name}",
                                message = "You have utilized $percent% of your ${budget.name} budget limit.",
                                priority = InsightPriority.MEDIUM,
                                categoryName = budget.categoryName
                            )
                        )
                    }
                }
            }

            insights
        }
    }
}

