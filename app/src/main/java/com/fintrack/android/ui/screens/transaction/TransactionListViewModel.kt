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

private data class TxFilterState(
    val query: String,
    val type: TransactionType?,
    val accountId: Long?,
    val categoryId: Long?
)

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
        _searchQuery,
        _selectedType,
        _selectedAccountId,
        _selectedCategoryId
    ) { query, type, accountId, categoryId ->
        TxFilterState(query, type, accountId, categoryId)
    }.flatMapLatest { filters ->
        combine(
            transactionRepository.getAllTransactions(),
            accountRepository.getAllAccounts(),
            categoryRepository.getAllCategories(),
            preferencesManager.userPreferencesFlow
        ) { txs, accounts, categories, prefs ->
            var filtered = txs

            if (filters.query.isNotBlank()) {
                filtered = filtered.filter {
                    it.notes.contains(filters.query, ignoreCase = true) ||
                            it.categoryName.contains(filters.query, ignoreCase = true) ||
                            it.accountName.contains(filters.query, ignoreCase = true) ||
                            it.amount.toPlainString().contains(filters.query)
                }
            }

            if (filters.type != null) {
                filtered = filtered.filter { it.type == filters.type }
            }

            if (filters.accountId != null) {
                filtered = filtered.filter {
                    it.accountId == filters.accountId || it.toAccountId == filters.accountId
                }
            }

            if (filters.categoryId != null) {
                filtered = filtered.filter { it.categoryId == filters.categoryId }
            }

            TransactionListUiState(
                transactions = filtered,
                accounts = accounts,
                categories = categories,
                searchQuery = filters.query,
                selectedType = filters.type,
                selectedAccountId = filters.accountId,
                selectedCategoryId = filters.categoryId,
                defaultCurrency = prefs.defaultCurrency,
                isBikramSambatEnabled = prefs.isBikramSambatEnabled
            )
        }
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

