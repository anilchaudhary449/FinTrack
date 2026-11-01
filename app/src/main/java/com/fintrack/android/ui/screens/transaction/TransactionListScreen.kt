package com.fintrack.android.ui.screens.transaction

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.ui.components.*
import com.fintrack.android.ui.screens.dashboard.TransactionItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactionDetails: (Long) -> Unit,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var transactionToDelete by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Transactions",
                actions = {
                    IconButton(onClick = onNavigateToAddTransaction) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTransaction,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search description, category, amount...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            // Filter Chips Row
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.selectedType == null,
                    onClick = { viewModel.onTypeFilterChange(null) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = state.selectedType == TransactionType.INCOME,
                    onClick = { viewModel.onTypeFilterChange(TransactionType.INCOME) },
                    label = { Text("Income") }
                )
                FilterChip(
                    selected = state.selectedType == TransactionType.EXPENSE,
                    onClick = { viewModel.onTypeFilterChange(TransactionType.EXPENSE) },
                    label = { Text("Expense") }
                )
                FilterChip(
                    selected = state.selectedType == TransactionType.TRANSFER,
                    onClick = { viewModel.onTypeFilterChange(TransactionType.TRANSFER) },
                    label = { Text("Transfer") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Transaction list
            if (state.transactions.isEmpty()) {
                FinEmptyState(
                    icon = Icons.Default.ReceiptLong,
                    title = "No Transactions Found",
                    message = "Try clearing your filters or search keywords, or add a new transaction.",
                    actionButtonText = "Add Transaction",
                    onActionClick = onNavigateToAddTransaction
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.transactions, key = { it.id }) { tx ->
                        TransactionItemRow(
                            transaction = tx,
                            isBikramSambat = state.isBikramSambatEnabled,
                            onClick = { onNavigateToTransactionDetails(tx.id) }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (transactionToDelete != null) {
        FinConfirmationDialog(
            title = "Delete Transaction",
            message = "Are you sure you want to delete this transaction? Its financial impact will be safely reverted from your account balance.",
            onConfirm = {
                transactionToDelete?.let { viewModel.deleteTransaction(it) }
                transactionToDelete = null
            },
            onDismiss = { transactionToDelete = null }
        )
    }
}

