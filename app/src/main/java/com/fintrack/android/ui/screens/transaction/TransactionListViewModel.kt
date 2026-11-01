package com.fintrack.android.ui.screens.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.Category
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.CategoryRepository
import com.fintrack.android.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val selectedType: TransactionType? = null,
    val selectedAccountId: Long? = null,
    val selectedCategoryId: Long? = null,
    val defaultCurrency: String = "NPR",
    val isBikramSambatEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedType = MutableStateFlow<TransactionType?>(null)
    private val _selectedAccountId = MutableStateFlow<Long?>(null)
    private val _selectedCategoryId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<TransactionListUiState> = combine(
        transactionRepository.getAllTransactions(),
        accountRepository.getAllAccounts(),
        categoryRepository.getAllCategories(),
        preferencesManager.userPreferencesFlow,
        _searchQuery,
        _selectedType,
        _selectedAccountId,
        _selectedCategoryId
    ) { txs, accounts, categories, prefs, query, type, accountId, categoryId ->
        var filtered = txs

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.notes.contains(query, ignoreCase = true) ||
                        it.categoryName.contains(query, ignoreCase = true) ||
                        it.accountName.contains(query, ignoreCase = true) ||
                        it.amount.toPlainString().contains(query)
            }
        }

        if (type != null) {
            filtered = filtered.filter { it.type == type }
        }

        if (accountId != null) {
            filtered = filtered.filter { it.accountId == accountId || it.toAccountId == accountId }
        }

        if (categoryId != null) {
            filtered = filtered.filter { it.categoryId == categoryId }
        }

        TransactionListUiState(
            transactions = filtered,
            accounts = accounts,
            categories = categories,
            searchQuery = query,
            selectedType = type,
            selectedAccountId = accountId,
            selectedCategoryId = categoryId,
            defaultCurrency = prefs.defaultCurrency,
            isBikramSambatEnabled = prefs.isBikramSambatEnabled
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TransactionListUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTypeFilterChange(type: TransactionType?) {
        _selectedType.value = type
    }

    fun onAccountFilterChange(accountId: Long?) {
        _selectedAccountId.value = accountId
    }

    fun onCategoryFilterChange(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(id)
        }
    }
}

