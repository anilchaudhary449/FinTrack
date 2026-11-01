package com.fintrack.android.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

data class SavingsGoal(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val targetAmount: BigDecimal,
    val currentSavedAmount: BigDecimal = BigDecimal.ZERO,
    val targetDate: Long,
    val isCompleted: Boolean = false,
    val iconName: String = "savings_default",
    val colorHex: String = "#10B981",
    val createdAt: Long = System.currentTimeMillis()
) {
    val progressPercentage: Float
        get() = if (targetAmount.compareTo(BigDecimal.ZERO) > 0) {
            currentSavedAmount.divide(targetAmount, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal(100))
                .toFloat()
                .coerceIn(0f, 100f)
        } else 0f

    val remainingAmount: BigDecimal
        get() = (targetAmount - currentSavedAmount).max(BigDecimal.ZERO)
}

data class SavingsContribution(
    val id: Long = 0,
    val savingsGoalId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val contributionDate: Long = System.currentTimeMillis(),
    val notes: String = ""
)

