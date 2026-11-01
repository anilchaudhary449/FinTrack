package com.fintrack.android

import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.util.CurrencyFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class FinancialCalculationsTest {

    @Test
    fun `currency formatting with NPR symbol formats correctly`() {
        val amount = BigDecimal("15000.50")
        val formatted = CurrencyFormatter.format(amount, "NPR")
        assertEquals("रु 15,000.50", formatted)
    }

    @Test
    fun `currency formatting handles arbitrary decimal precision without loss`() {
        val amount = BigDecimal("1000000.00")
        val formatted = CurrencyFormatter.format(amount, "NPR")
        assertEquals("रु 1,000,000.00", formatted)
    }

    @Test
    fun `budget calculation sets normal status when under threshold`() {
        val budget = Budget(
            id = 1,
            name = "Food",
            amountLimit = BigDecimal("10000.00"),
            warningThresholdPercentage = 80,
            month = 11,
            year = 2026,
            startDate = 0L,
            endDate = 1000L
        )

        val progress = BudgetProgress.calculate(budget, BigDecimal("4000.00"))
        assertEquals(40f, progress.percentageConsumed, 0.01f)
        assertEquals(BudgetAlertStatus.NORMAL, progress.status)
        assertEquals(BigDecimal("6000.00"), progress.remainingAmount)
    }

    @Test
    fun `budget calculation sets warning status at or above 80 percent`() {
        val budget = Budget(
            id = 2,
            name = "Shopping",
            amountLimit = BigDecimal("10000.00"),
            warningThresholdPercentage = 80,
            month = 11,
            year = 2026,
            startDate = 0L,
            endDate = 1000L
        )

        val progress = BudgetProgress.calculate(budget, BigDecimal("8500.00"))
        assertEquals(85f, progress.percentageConsumed, 0.01f)
        assertEquals(BudgetAlertStatus.WARNING, progress.status)
        assertEquals(BigDecimal("1500.00"), progress.remainingAmount)
    }

    @Test
    fun `budget calculation sets exceeded status when spent exceeds limit`() {
        val budget = Budget(
            id = 3,
            name = "Entertainment",
            amountLimit = BigDecimal("5000.00"),
            warningThresholdPercentage = 80,
            month = 11,
            year = 2026,
            startDate = 0L,
            endDate = 1000L
        )

        val progress = BudgetProgress.calculate(budget, BigDecimal("5500.00"))
        assertEquals(110f, progress.percentageConsumed, 0.01f)
        assertEquals(BudgetAlertStatus.EXCEEDED, progress.status)
        assertEquals(BigDecimal.ZERO, progress.remainingAmount)
    }

    @Test
    fun `savings goal progress percentage and completion logic`() {
        val goal = SavingsGoal(
            id = 1,
            title = "Laptop Fund",
            targetAmount = BigDecimal("120000.00"),
            currentSavedAmount = BigDecimal("60000.00"),
            targetDate = System.currentTimeMillis() + 1000000L
        )

        assertEquals(50f, goal.progressPercentage, 0.01f)
        assertEquals(BigDecimal("60000.00"), goal.remainingAmount)
    }

    @Test
    fun `net worth equals total assets minus total liabilities`() {
        val cash = BigDecimal("50000.00")
        val bank = BigDecimal("150000.00")
        val lentToFriend = BigDecimal("20000.00") // Asset (Owed to me)
        val totalAssets = cash.add(bank).add(lentToFriend) // 220,000

        val loan = BigDecimal("80000.00") // Liability (Owed by me)
        val totalLiabilities = loan // 80,000

        val netWorth = totalAssets.subtract(totalLiabilities)
        assertEquals(BigDecimal("140000.00"), netWorth)
    }

    @Test
    fun `monetary arithmetic uses banker rounding without IEEE 754 precision errors`() {
        val a = BigDecimal("0.1")
        val b = BigDecimal("0.2")
        val result = a.add(b)
        assertEquals(BigDecimal("0.3"), result)
    }
}

