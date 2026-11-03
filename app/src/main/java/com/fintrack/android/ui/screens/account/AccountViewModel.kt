package com.fintrack.android.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.AccountType
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.ExchangeRateRepository
import com.fintrack.android.domain.repository.ReconciliationRepository
import kotlinx.coroutines.flow.combine
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
    val displayCurrency: String = "NPR",
    val isAddAccountOpen: Boolean = false,
    val isTransferOpen: Boolean = false,
    val isReconcileOpen: Boolean = false,
    val selectedAccountForReconcile: Account? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val reconciliationRepository: ReconciliationRepository,
    private val transferFundsUseCase: TransferFundsUseCase,
    private val preferencesManager: UserPreferencesManager,
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadAccounts()
        viewModelScope.launch { exchangeRateRepository.refreshIfStale() }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            combine(
                accountRepository.getAllAccounts(),
                preferencesManager.userPreferencesFlow,
                exchangeRateRepository.snapshot
            ) { accounts, preferences, rates ->
                val total = accounts.fold(BigDecimal.ZERO) { acc, account ->
                    val converted = rates?.convert(
                        account.currentBalance,
                        account.currency,
                        preferences.defaultCurrency
                    ) ?: account.currentBalance
                    acc.add(converted)
                }
                Triple(accounts, total, preferences.defaultCurrency)
            }.collect { (accounts, total, currency) ->
                _uiState.update {
                    it.copy(accounts = accounts, totalBalance = total, displayCurrency = currency)
                }
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
            runCatching {
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
            }.onSuccess {
                _uiState.update { it.copy(successMessage = "Account created", errorMessage = null) }
                closeAddAccount()
            }.onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message ?: "Could not create account") }
            }
        }
    }

    fun transferMoney(fromId: Long, toId: Long, amount: BigDecimal, notes: String) {
        viewModelScope.launch {
            try {
                transferFundsUseCase(fromId, toId, amount, notes)
                _uiState.update { it.copy(successMessage = "Transfer completed", errorMessage = null) }
                closeTransfer()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Transfer failed") }
            }
        }
    }

    fun reconcileAccount(actualBalance: BigDecimal, notes: String) {
        val account = _uiState.value.selectedAccountForReconcile ?: return
        viewModelScope.launch {
            try {
                reconciliationRepository.reconcileAccount(account.id, actualBalance, notes)
                _uiState.update { it.copy(successMessage = "Account reconciled", errorMessage = null) }
                closeReconciliation()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Reconciliation failed") }
            }
        }
    }
}

