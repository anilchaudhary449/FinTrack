package com.fintrack.android.ui.screens.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.AccountType
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.IncomeGreen
import com.fintrack.android.ui.theme.TransferBlue
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onNavigateBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Account Management",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.openTransfer() }) {
                        Icon(Icons.Default.SyncAlt, contentDescription = "Transfer")
                    }
                    IconButton(onClick = { viewModel.openAddAccount() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Account")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddAccount() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Balance Banner
            item {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Net Liquid Balance Across Accounts",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CurrencyFormatter.format(state.totalBalance),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Quick Transfer CTA
            item {
                OutlinedButton(
                    onClick = { viewModel.openTransfer() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.SyncAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Transfer Funds Between Accounts", fontWeight = FontWeight.Bold)
                }
            }

            item {
                Text(
                    text = "Financial Accounts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.accounts, key = { it.id }) { acc ->
                AccountCardItem(
                    account = acc,
                    onReconcile = { viewModel.openReconciliation(acc) }
                )
            }
        }
    }

    // Add Account Dialog
    if (state.isAddAccountOpen) {
        AddAccountDialog(
            onDismiss = { viewModel.closeAddAccount() },
            onConfirm = { name, type, bal, curr, desc ->
                viewModel.createAccount(name, type, bal, curr, desc)
            }
        )
    }

    // Transfer Dialog
    if (state.isTransferOpen) {
        TransferFundsDialog(
            accounts = state.accounts,
            errorMessage = state.errorMessage,
            onDismiss = { viewModel.closeTransfer() },
            onConfirm = { fromId, toId, amount, notes ->
                viewModel.transferMoney(fromId, toId, amount, notes)
            }
        )
    }

    // Reconciliation Dialog
    if (state.isReconcileOpen && state.selectedAccountForReconcile != null) {
        ReconciliationDialog(
            account = state.selectedAccountForReconcile!!,
            errorMessage = state.errorMessage,
            onDismiss = { viewModel.closeReconciliation() },
            onConfirm = { actualBal, notes ->
                viewModel.reconcileAccount(actualBal, notes)
            }
        )
    }
}

@Composable
private fun AccountCardItem(
    account: Account,
    onReconcile: () -> Unit
) {
    FinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = account.type.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (account.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = account.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.format(account.currentBalance, account.currency),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (account.currentBalance >= BigDecimal.ZERO) IncomeGreen else MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextButton(
                    onClick = onReconcile,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reconcile", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun AddAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, AccountType, BigDecimal, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(AccountType.BANK_ACCOUNT) }
    var balanceStr by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("NPR") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Financial Account") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Account Name (e.g. NIC Asia Bank)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = balanceStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) balanceStr = it },
                    label = { Text("Opening Balance") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description (Optional)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val bal = runCatching { BigDecimal(balanceStr) }.getOrDefault(BigDecimal.ZERO)
                    if (name.isNotBlank()) {
                        onConfirm(name, type, bal, currency, desc)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun TransferFundsDialog(
    accounts: List<Account>,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onConfirm: (Long, Long, BigDecimal, String) -> Unit
) {
    var fromAccountId by remember { mutableStateOf(accounts.firstOrNull()?.id ?: 0L) }
    var toAccountId by remember { mutableStateOf(accounts.getOrNull(1)?.id ?: 0L) }
    var amountStr by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Transfer Funds") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (errorMessage != null) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Text("From Account:", style = MaterialTheme.typography.labelSmall)
                accounts.forEach { acc ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { fromAccountId = acc.id }
                    ) {
                        RadioButton(selected = fromAccountId == acc.id, onClick = { fromAccountId = acc.id })
                        Text("${acc.name} (${CurrencyFormatter.format(acc.currentBalance)})")
                    }
                }

                Text("To Account:", style = MaterialTheme.typography.labelSmall)
                accounts.filter { it.id != fromAccountId }.forEach { acc ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { toAccountId = acc.id }
                    ) {
                        RadioButton(selected = toAccountId == acc.id, onClick = { toAccountId = acc.id })
                        Text("${acc.name} (${CurrencyFormatter.format(acc.currentBalance)})")
                    }
                }

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) amountStr = it },
                    label = { Text("Transfer Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = runCatching { BigDecimal(amountStr) }.getOrNull()
                    if (amount != null && amount > BigDecimal.ZERO && fromAccountId != toAccountId) {
                        onConfirm(fromAccountId, toAccountId, amount, notes)
                    }
                }
            ) {
                Text("Transfer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ReconciliationDialog(
    account: Account,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onConfirm: (BigDecimal, String) -> Unit
) {
    var statementBalStr by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val actualBal = runCatching { BigDecimal(statementBalStr) }.getOrNull()
    val discrepancy = if (actualBal != null) actualBal.subtract(account.currentBalance) else BigDecimal.ZERO

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reconcile ${account.name}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "App Calculated Balance: ${CurrencyFormatter.format(account.currentBalance, account.currency)}",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = statementBalStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^-?\\d*\\.?\\d{0,2}\$"))) statementBalStr = it },
                    label = { Text("Actual Statement/Wallet Balance") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                if (actualBal != null) {
                    val color = if (discrepancy.compareTo(BigDecimal.ZERO) == 0) IncomeGreen else MaterialTheme.colorScheme.error
                    Text(
                        text = "Discrepancy: ${CurrencyFormatter.format(discrepancy, account.currency)}",
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Reconciliation Note") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (actualBal != null) {
                        onConfirm(actualBal, notes)
                    }
                }
            ) {
                Text("Reconcile & Adjust")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

