package com.fintrack.android.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.TransactionRepository
import com.fintrack.android.domain.usecase.CalculateNetWorthUseCase
import com.fintrack.android.domain.usecase.GetDashboardSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

data class DashboardUiState(
    val selectedTimeRange: TimeRange = TimeRange.THIS_MONTH,
    val summary: FinancialSummary = FinancialSummary(
        totalBalance = BigDecimal.ZERO,
        totalIncome = BigDecimal.ZERO,
        totalExpenses = BigDecimal.ZERO,
        netSavings = BigDecimal.ZERO,
        budgetUtilizationPercentage = 0f,
        totalDebtsOwedByMe = BigDecimal.ZERO,
        totalDebtsOwedToMe = BigDecimal.ZERO,
        upcomingBillsCount = 0
    ),
    val netWorth: NetWorthSummary = NetWorthSummary(
        totalAssets = BigDecimal.ZERO,
        totalLiabilities = BigDecimal.ZERO,
        netWorth = BigDecimal.ZERO,
        cashAndBankAssets = BigDecimal.ZERO,
        digitalWalletAssets = BigDecimal.ZERO,
        savingsAndInvestments = BigDecimal.ZERO,
        moneyLentAssets = BigDecimal.ZERO,
        loanLiabilities = BigDecimal.ZERO,
        creditCardLiabilities = BigDecimal.ZERO
    ),
    val recentTransactions: List<Transaction> = emptyList(),
    val categorySpending: List<CategorySpending> = emptyList(),
    val defaultCurrency: String = "NPR",
    val isBikramSambatEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val calculateNetWorthUseCase: CalculateNetWorthUseCase,
    private val transactionRepository: TransactionRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _selectedTimeRange = MutableStateFlow(TimeRange.THIS_MONTH)
    private val _customStart = MutableStateFlow<Long?>(null)
    private val _customEnd = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<DashboardUiState> = combine(
        _selectedTimeRange,
        preferencesManager.userPreferencesFlow,
        calculateNetWorthUseCase(),
        transactionRepository.getRecentTransactions(8)
    ) { timeRange, prefs, netWorth, recentTxs ->
        Tuple4(timeRange, prefs, netWorth, recentTxs)
    }.flatMapLatest { (timeRange, prefs, netWorth, recentTxs) ->
        getDashboardSummaryUseCase(timeRange, _customStart.value, _customEnd.value).map { summary ->
            val spendingList = calculateCategoryBreakdown(recentTxs, summary.totalExpenses)
            DashboardUiState(
                selectedTimeRange = timeRange,
                summary = summary,
                netWorth = netWorth,
                recentTransactions = recentTxs,
                categorySpending = spendingList,
                defaultCurrency = prefs.defaultCurrency,
                isBikramSambatEnabled = prefs.isBikramSambatEnabled
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardUiState()
    )

    fun onTimeRangeSelected(range: TimeRange, customStart: Long? = null, customEnd: Long? = null) {
        _selectedTimeRange.value = range
        _customStart.value = customStart
        _customEnd.value = customEnd
    }

    private fun calculateCategoryBreakdown(transactions: List<Transaction>, totalExpense: BigDecimal): List<CategorySpending> {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        if (expenses.isEmpty() || totalExpense.compareTo(BigDecimal.ZERO) <= 0) return emptyList()

        return expenses.groupBy { it.categoryId ?: 0L }
            .map { (catId, txs) ->
                val first = txs.first()
                val sum = txs.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }
                val percentage = sum.divide(totalExpense, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal(100)).toFloat()
                CategorySpending(
                    categoryId = catId,
                    categoryName = first.categoryName.ifEmpty { "General" },
                    colorHex = first.categoryColor.ifEmpty { "#EF4444" },
                    iconName = first.categoryIcon,
                    totalAmount = sum,
                    percentageOfTotal = percentage
                )
            }.sortedByDescending { it.totalAmount }
    }
}

private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

