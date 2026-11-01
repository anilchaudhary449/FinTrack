package com.fintrack.android.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

enum class BudgetAlertStatus {
    NORMAL,
    WARNING,
    EXCEEDED
}

data class Budget(
    val id: Long = 0,
    val name: String,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val amountLimit: BigDecimal,
    val warningThresholdPercentage: Int = 80,
    val month: Int, // 1 - 12
    val year: Int,
    val startDate: Long,
    val endDate: Long,
    val createdAt: Long = System.currentTimeMillis()
)

data class BudgetProgress(
    val budget: Budget,
    val spentAmount: BigDecimal,
    val remainingAmount: BigDecimal,
    val percentageConsumed: Float,
    val status: BudgetAlertStatus
) {
    companion object {
        fun calculate(budget: Budget, spent: BigDecimal): BudgetProgress {
            val spentSafe = spent.max(BigDecimal.ZERO)
            val remaining = (budget.amountLimit - spentSafe).max(BigDecimal.ZERO)
            val percentage = if (budget.amountLimit.compareTo(BigDecimal.ZERO) > 0) {
                spentSafe.divide(budget.amountLimit, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal(100))
                    .toFloat()
            } else 0f

            val status = when {
                percentage >= 100f -> BudgetAlertStatus.EXCEEDED
                percentage >= budget.warningThresholdPercentage -> BudgetAlertStatus.WARNING
                else -> BudgetAlertStatus.NORMAL
            }

            return BudgetProgress(
                budget = budget,
                spentAmount = spentSafe,
                remainingAmount = remaining,
                percentageConsumed = percentage,
                status = status
            )
        }
    }
}

