package com.fintrack.android.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.Debt
import com.fintrack.android.domain.model.RecurringTransaction
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.DebtRepository
import com.fintrack.android.domain.repository.RecurringRepository
import com.fintrack.android.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class CalendarDayData(
    val date: LocalDate,
    val dayOfMonth: Int,
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val hasUpcomingBill: Boolean = false,
    val hasLoanDue: Boolean = false
)

data class CalendarUiState(
    val currentYearMonth: LocalDate = LocalDate.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val daysInMonth: List<CalendarDayData> = emptyList(),
    val selectedDateTransactions: List<Transaction> = emptyList(),
    val selectedDateIncome: BigDecimal = BigDecimal.ZERO,
    val selectedDateExpense: BigDecimal = BigDecimal.ZERO
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val recurringRepository: RecurringRepository,
    private val debtRepository: DebtRepository
) : ViewModel() {

    private val _currentYearMonth = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    val uiState: StateFlow<CalendarUiState> = combine(
        _currentYearMonth,
        _selectedDate,
        transactionRepository.getAllTransactions(),
        recurringRepository.getAllRecurring(),
        debtRepository.getAllDebts()
    ) { yearMonth, selected, allTxs, recurring, debts ->
        val zone = ZoneId.systemDefault()
        val daysCount = yearMonth.lengthOfMonth()
        val daysList = (1..daysCount).map { day ->
            val date = yearMonth.withDayOfMonth(day)
            val startOfDay = date.atStartOfDay(zone).toInstant().toEpochMilli()
            val endOfDay = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

            val dayTxs = allTxs.filter { it.transactionDate in startOfDay..endOfDay }
            val inc = dayTxs.filter { it.type == TransactionType.INCOME }
                .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }
            val exp = dayTxs.filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }

            val hasBill = recurring.any { it.isActive && it.nextDueDate in startOfDay..endOfDay }
            val hasLoan = debts.any { it.dueDate != null && it.dueDate in startOfDay..endOfDay }

            CalendarDayData(
                date = date,
                dayOfMonth = day,
                totalIncome = inc,
                totalExpense = exp,
                hasUpcomingBill = hasBill,
                hasLoanDue = hasLoan
            )
        }

        val selectedStart = selected.atStartOfDay(zone).toInstant().toEpochMilli()
        val selectedEnd = selected.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        val selectedTxs = allTxs.filter { it.transactionDate in selectedStart..selectedEnd }
        val selInc = selectedTxs.filter { it.type == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }
        val selExp = selectedTxs.filter { it.type == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }

        CalendarUiState(
            currentYearMonth = yearMonth,
            selectedDate = selected,
            daysInMonth = daysList,
            selectedDateTransactions = selectedTxs,
            selectedDateIncome = selInc,
            selectedDateExpense = selExp
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CalendarUiState()
    )

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
        _selectedDate.value = _currentYearMonth.value
    }

    fun onNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
        _selectedDate.value = _currentYearMonth.value
    }
}

