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
import com.fintrack.android.domain.util.CurrencyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
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

    private val _filters = MutableStateFlow(TxFilterState("", null, null, null))

    val uiState: StateFlow<TransactionListUiState> = combine(
        _filters,
        transactionRepository.getAllTransactions(),
        accountRepository.getAllAccounts(),
        categoryRepository.getAllCategories(),
        preferencesManager.userPreferencesFlow
    ) { filters, txs, accounts, categories, prefs ->
        val query = normalizeSearchText(filters.query)
        val filtered = txs.filter { transaction ->
            val matchesQuery = query.isEmpty() || listOf(
                transaction.notes,
                transaction.categoryName,
                transaction.accountName,
                transaction.paymentMethod.displayName,
                transaction.type.name,
                transaction.currency,
                transaction.amount.toPlainString(),
                CurrencyFormatter.format(transaction.amount, transaction.currency, includeSymbol = false)
            ).any { normalizeSearchText(it).contains(query) }
            val matchesType = filters.type == null || transaction.type == filters.type
            val matchesAccount = filters.accountId == null ||
                    transaction.accountId == filters.accountId || transaction.toAccountId == filters.accountId
            val matchesCategory = filters.categoryId == null || transaction.categoryId == filters.categoryId
            matchesQuery && matchesType && matchesAccount && matchesCategory
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
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TransactionListUiState()
    )

    fun onSearchQueryChange(query: String) {
        _filters.update { it.copy(query = query) }
    }

    fun onTypeFilterChange(type: TransactionType?) {
        _filters.update { it.copy(type = type) }
    }

    fun onAccountFilterChange(accountId: Long?) {
        _filters.update { it.copy(accountId = accountId) }
    }

    fun onCategoryFilterChange(categoryId: Long?) {
        _filters.update { it.copy(categoryId = categoryId) }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(id)
        }
    }

    private fun normalizeSearchText(value: String): String =
        value.trim().replace(",", "").lowercase(Locale.ROOT)
}

