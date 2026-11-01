package com.fintrack.android.ui.screens.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.Budget
import com.fintrack.android.domain.model.BudgetProgress
import com.fintrack.android.domain.model.Category
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.BudgetRepository
import com.fintrack.android.domain.repository.CategoryRepository
import com.fintrack.android.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class BudgetUiState(
    val budgetProgressList: List<BudgetProgress> = emptyList(),
    val categories: List<Category> = emptyList(),
    val totalBudgetLimit: BigDecimal = BigDecimal.ZERO,
    val totalSpent: BigDecimal = BigDecimal.ZERO,
    val isAddDialogOpen: Boolean = false,
    val selectedMonth: Int = LocalDate.now().monthValue,
    val selectedYear: Int = LocalDate.now().year,
    val errorMessage: String? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _month = MutableStateFlow(LocalDate.now().monthValue)
    private val _year = MutableStateFlow(LocalDate.now().year)
    private val _isAddDialogOpen = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BudgetUiState> = combine(
        _month,
        _year,
        _isAddDialogOpen,
        _errorMessage,
        categoryRepository.getAllCategories()
    ) { month, year, isOpen, error, categories ->
        Tuple5(month, year, isOpen, error, categories)
    }.flatMapLatest { (month, year, isOpen, error, categories) ->
        val zone = ZoneId.systemDefault()
        val startOfMonth = LocalDate.of(year, month, 1).atStartOfDay(zone).toInstant().toEpochMilli()
        val endOfMonth = LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        combine(
            budgetRepository.getBudgetsForMonth(month, year),
            transactionRepository.getTransactionsBetweenDates(startOfMonth, endOfMonth)
        ) { budgets, transactions ->
            val expenses = transactions.filter { it.type == TransactionType.EXPENSE }

            val progressList = budgets.map { budget ->
                val spent = if (budget.categoryId != null) {
                    expenses.filter { it.categoryId == budget.categoryId }
                        .fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }
                } else {
                    expenses.fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }
                }
                BudgetProgress.calculate(budget, spent)
            }

            val totalLimit = budgets.fold(BigDecimal.ZERO) { acc, b -> acc.add(b.amountLimit) }
            val totalSpent = expenses.fold(BigDecimal.ZERO) { acc, tx -> acc.add(tx.amount) }

            BudgetUiState(
                budgetProgressList = progressList,
                categories = categories,
                totalBudgetLimit = totalLimit,
                totalSpent = totalSpent,
                isAddDialogOpen = isOpen,
                selectedMonth = month,
                selectedYear = year,
                errorMessage = error
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BudgetUiState()
    )

    fun openAddDialog() { _isAddDialogOpen.value = true }
    fun closeAddDialog() { _isAddDialogOpen.value = false }

    fun createBudget(name: String, categoryId: Long?, amountLimit: BigDecimal, threshold: Int) {
        viewModelScope.launch {
            val now = LocalDate.of(_year.value, _month.value, 1)
            val zone = ZoneId.systemDefault()
            val start = now.atStartOfDay(zone).toInstant().toEpochMilli()
            val end = now.plusMonths(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

            budgetRepository.createBudget(
                Budget(
                    name = name,
                    categoryId = categoryId,
                    amountLimit = amountLimit,
                    warningThresholdPercentage = threshold,
                    month = _month.value,
                    year = _year.value,
                    startDate = start,
                    endDate = end
                )
            )
            closeAddDialog()
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }
}

private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)

