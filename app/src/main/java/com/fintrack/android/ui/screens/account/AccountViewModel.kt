package com.fintrack.android.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.AccountType
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.ReconciliationRepository
import com.fintrack.android.domain.usecase.TransferFundsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class AccountUiState(
    val accounts: List<Account> = emptyList(),
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val isAddAccountOpen: Boolean = false,
    val isTransferOpen: Boolean = false,
    val isReconcileOpen: Boolean = false,
    val selectedAccountForReconcile: Account? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val reconciliationRepository: ReconciliationRepository,
    private val transferFundsUseCase: TransferFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { list ->
                val total = list.fold(BigDecimal.ZERO) { acc, a -> acc.add(a.currentBalance) }
                _uiState.update { it.copy(accounts = list, totalBalance = total) }
            }
        }
    }

    fun openAddAccount() = _uiState.update { it.copy(isAddAccountOpen = true, errorMessage = null) }
    fun closeAddAccount() = _uiState.update { it.copy(isAddAccountOpen = false) }

    fun openTransfer() = _uiState.update { it.copy(isTransferOpen = true, errorMessage = null) }
    fun closeTransfer() = _uiState.update { it.copy(isTransferOpen = false) }

    fun openReconciliation(account: Account) {
        _uiState.update {
            it.copy(
                isReconcileOpen = true,
                selectedAccountForReconcile = account,
                errorMessage = null
            )
        }
    }
    fun closeReconciliation() {
        _uiState.update { it.copy(isReconcileOpen = false, selectedAccountForReconcile = null) }
    }

    fun createAccount(name: String, type: AccountType, openingBalance: BigDecimal, currency: String, desc: String) {
        viewModelScope.launch {
            accountRepository.createAccount(
                Account(
                    name = name,
                    type = type,
                    openingBalance = openingBalance,
                    currentBalance = openingBalance,
                    currency = currency,
                    description = desc
                )
            )
            closeAddAccount()
        }
    }

    fun transferMoney(fromId: Long, toId: Long, amount: BigDecimal, notes: String) {
        viewModelScope.launch {
            try {
                transferFundsUseCase(fromId, toId, amount, notes)
                closeTransfer()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun reconcileAccount(actualBalance: BigDecimal, notes: String) {
        val account = _uiState.value.selectedAccountForReconcile ?: return
        viewModelScope.launch {
            try {
                reconciliationRepository.reconcileAccount(account.id, actualBalance, notes)
                closeReconciliation()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
}

