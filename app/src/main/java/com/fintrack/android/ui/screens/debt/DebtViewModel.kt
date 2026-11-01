package com.fintrack.android.ui.screens.debt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.DebtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class DebtUiState(
    val debts: List<Debt> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val selectedTab: DebtType = DebtType.OWED_BY_ME,
    val totalOwedByMe: BigDecimal = BigDecimal.ZERO,
    val totalOwedToMe: BigDecimal = BigDecimal.ZERO,
    val isAddDebtOpen: Boolean = false,
    val selectedDebtForPayment: Debt? = null
)

@HiltViewModel
class DebtViewModel @Inject constructor(
    private val debtRepository: DebtRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(DebtType.OWED_BY_ME)
    private val _isAddDebtOpen = MutableStateFlow(false)
    private val _selectedDebtForPayment = MutableStateFlow<Debt?>(null)

    val uiState: StateFlow<DebtUiState> = combine(
        debtRepository.getAllDebts(),
        accountRepository.getAllAccounts(),
        _selectedTab,
        _isAddDebtOpen,
        _selectedDebtForPayment
    ) { debts, accounts, tab, isAddOpen, debtForPayment ->
        val owedByMe = debts.filter { it.type == DebtType.OWED_BY_ME && it.status != DebtStatus.SETTLED }
            .fold(BigDecimal.ZERO) { acc, d -> acc.add(d.outstandingAmount) }
        val owedToMe = debts.filter { it.type == DebtType.OWED_TO_ME && it.status != DebtStatus.SETTLED }
            .fold(BigDecimal.ZERO) { acc, d -> acc.add(d.outstandingAmount) }

        val filtered = debts.filter { it.type == tab }

        DebtUiState(
            debts = filtered,
            accounts = accounts,
            selectedTab = tab,
            totalOwedByMe = owedByMe,
            totalOwedToMe = owedToMe,
            isAddDebtOpen = isAddOpen,
            selectedDebtForPayment = debtForPayment
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DebtUiState()
    )

    fun onTabSelected(tab: DebtType) { _selectedTab.value = tab }
    fun openAddDebt() { _isAddDebtOpen.value = true }
    fun closeAddDebt() { _isAddDebtOpen.value = false }

    fun openRecordPayment(debt: Debt) { _selectedDebtForPayment.value = debt }
    fun closeRecordPayment() { _selectedDebtForPayment.value = null }

    fun createDebt(
        personName: String,
        type: DebtType,
        amount: BigDecimal,
        interestRate: BigDecimal,
        dueDate: Long?,
        accountId: Long?,
        notes: String
    ) {
        viewModelScope.launch {
            debtRepository.createDebt(
                Debt(
                    personName = personName,
                    type = type,
                    originalAmount = amount,
                    outstandingAmount = amount,
                    interestRate = interestRate,
                    dueDate = dueDate,
                    accountId = accountId,
                    notes = notes
                )
            )
            closeAddDebt()
        }
    }

    fun recordPayment(debtId: Long, accountId: Long, amount: BigDecimal, notes: String) {
        viewModelScope.launch {
            debtRepository.recordDebtPayment(
                DebtPayment(
                    debtId = debtId,
                    accountId = accountId,
                    amount = amount,
                    notes = notes
                )
            )
            closeRecordPayment()
        }
    }
}

