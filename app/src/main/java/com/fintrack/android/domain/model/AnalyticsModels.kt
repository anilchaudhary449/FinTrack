package com.fintrack.android.domain.model

import java.math.BigDecimal

data class NetWorthSummary(
    val totalAssets: BigDecimal,
    val totalLiabilities: BigDecimal,
    val netWorth: BigDecimal,
    val cashAndBankAssets: BigDecimal,
    val digitalWalletAssets: BigDecimal,
    val savingsAndInvestments: BigDecimal,
    val moneyLentAssets: BigDecimal, // Debts owed to user
    val loanLiabilities: BigDecimal, // Debts owed by user
    val creditCardLiabilities: BigDecimal,
    val monthlyTrendPercentage: Float = 0f
)

enum class InsightPriority {
    LOW,
    MEDIUM,
    HIGH
}

data class SpendingInsight(
    val id: String,
    val title: String,
    val message: String,
    val priority: InsightPriority,
    val categoryName: String? = null,
    val diffPercentage: Float? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class FinancialSummary(
    val totalBalance: BigDecimal,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val netSavings: BigDecimal,
    val budgetUtilizationPercentage: Float,
    val totalDebtsOwedByMe: BigDecimal,
    val totalDebtsOwedToMe: BigDecimal,
    val upcomingBillsCount: Int,
    val timeRange: TimeRange = TimeRange.THIS_MONTH
)

data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val colorHex: String,
    val iconName: String,
    val totalAmount: BigDecimal,
    val percentageOfTotal: Float
)

