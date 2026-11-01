package com.fintrack.android.ui.screens.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.CategoryRepository
import com.fintrack.android.domain.repository.TransactionRepository
import com.fintrack.android.domain.usecase.TransferFundsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class AddEditTransactionUiState(
    val id: Long? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val amountString: String = "",
    val selectedCategoryId: Long? = null,
    val selectedAccountId: Long? = null,
    val selectedToAccountId: Long? = null,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val transactionDate: Long = System.currentTimeMillis(),
    val notes: String = "",
    val receiptUri: String? = null,
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transferFundsUseCase: TransferFundsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Long = savedStateHandle.get<Long>("id") ?: -1L
    private val initialTypeString: String? = savedStateHandle.get<String>("type")

    private val _uiState = MutableStateFlow(AddEditTransactionUiState())
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    init {
        val initialType = when (initialTypeString?.uppercase()) {
            "INCOME" -> TransactionType.INCOME
            "TRANSFER" -> TransactionType.TRANSFER
            else -> TransactionType.EXPENSE
        }
        _uiState.update { it.copy(type = initialType) }

        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val accounts = accountRepository.getAllAccounts().first()
            val categories = categoryRepository.getAllCategories().first()

            _uiState.update {
                it.copy(
                    accounts = accounts,
                    categories = categories,
                    selectedAccountId = accounts.firstOrNull()?.id,
                    selectedToAccountId = if (accounts.size > 1) accounts[1].id else null,
                    selectedCategoryId = categories.firstOrNull { c ->
                        if (it.type == TransactionType.INCOME) c.type == CategoryType.INCOME else c.type == CategoryType.EXPENSE
                    }?.id
                )
            }

            if (transactionId > 0) {
                val existing = transactionRepository.getTransactionById(transactionId)
                if (existing != null) {
                    _uiState.update {
                        it.copy(
                            id = existing.id,
                            type = existing.type,
                            amountString = existing.amount.toPlainString(),
                            selectedCategoryId = existing.categoryId,
                            selectedAccountId = existing.accountId,
                            selectedToAccountId = existing.toAccountId,
                            selectedPaymentMethod = existing.paymentMethod,
                            transactionDate = existing.transactionDate,
                            notes = existing.notes,
                            receiptUri = existing.receiptUri
                        )
                    }
                }
            }
        }
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.update { state ->
            val matchingCategory = state.categories.firstOrNull {
                if (type == TransactionType.INCOME) it.type == CategoryType.INCOME else it.type == CategoryType.EXPENSE
            }
            state.copy(
                type = type,
                selectedCategoryId = matchingCategory?.id ?: state.selectedCategoryId
            )
        }
    }

    fun onAmountChange(amount: String) {
        // Allow only valid numbers and at most 2 decimal digits
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
            _uiState.update { it.copy(amountString = amount, errorMessage = null) }
        }
    }

    fun onCategoryChange(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun onAccountChange(accountId: Long) {
        _uiState.update { it.copy(selectedAccountId = accountId) }
    }

    fun onToAccountChange(toAccountId: Long) {
        _uiState.update { it.copy(selectedToAccountId = toAccountId) }
    }

    fun onPaymentMethodChange(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun onDateChange(date: Long) {
        _uiState.update { it.copy(transactionDate = date) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onReceiptUriChange(uri: String?) {
        _uiState.update { it.copy(receiptUri = uri) }
    }

    fun saveTransaction() {
        val state = _uiState.value

        val amount = runCatching { BigDecimal(state.amountString) }.getOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
            _uiState.update { it.copy(errorMessage = "Please enter a valid amount greater than zero") }
            return
        }

        if (state.selectedAccountId == null) {
            _uiState.update { it.copy(errorMessage = "Please select an account") }
            return
        }

        if (state.type == TransactionType.TRANSFER) {
            if (state.selectedToAccountId == null || state.selectedToAccountId == state.selectedAccountId) {
                _uiState.update { it.copy(errorMessage = "Please select a different destination account") }
                return
            }
        }

        viewModelScope.launch {
            try {
                if (state.type == TransactionType.TRANSFER && state.id == null) {
                    transferFundsUseCase(
                        fromAccountId = state.selectedAccountId,
                        toAccountId = state.selectedToAccountId!!,
                        amount = amount,
                        notes = state.notes,
                        date = state.transactionDate
                    )
                } else {
                    val tx = Transaction(
                        id = state.id ?: 0L,
                        type = state.type,
                        amount = amount,
                        categoryId = if (state.type == TransactionType.TRANSFER) null else state.selectedCategoryId,
                        accountId = state.selectedAccountId,
                        toAccountId = if (state.type == TransactionType.TRANSFER) state.selectedToAccountId else null,
                        paymentMethod = state.selectedPaymentMethod,
                        transactionDate = state.transactionDate,
                        notes = state.notes,
                        receiptUri = state.receiptUri
                    )

                    if (state.id != null && state.id > 0) {
                        transactionRepository.updateTransaction(tx)
                    } else {
                        transactionRepository.addTransaction(tx)
                    }
                }

                _uiState.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to save transaction") }
            }
        }
    }
}

