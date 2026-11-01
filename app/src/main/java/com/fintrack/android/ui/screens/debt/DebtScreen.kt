package com.fintrack.android.ui.screens.debt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoneyOff
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
import com.fintrack.android.domain.model.Debt
import com.fintrack.android.domain.model.DebtStatus
import com.fintrack.android.domain.model.DebtType
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinEmptyState
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtScreen(
    onNavigateBack: () -> Unit,
    viewModel: DebtViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Debts & Loans",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.openAddDebt() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Debt/Loan")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDebt() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Debt")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs: Money I Owe vs Money Owed to Me
            TabRow(
                selectedTabIndex = if (state.selectedTab == DebtType.OWED_BY_ME) 0 else 1,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Tab(
                    selected = state.selectedTab == DebtType.OWED_BY_ME,
                    onClick = { viewModel.onTabSelected(DebtType.OWED_BY_ME) },
                    text = {
                        Text(
                            text = "Money I Owe (${CurrencyFormatter.format(state.totalOwedByMe)})",
                            fontWeight = FontWeight.Bold,
                            color = if (state.selectedTab == DebtType.OWED_BY_ME) ExpenseRed else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                Tab(
                    selected = state.selectedTab == DebtType.OWED_TO_ME,
                    onClick = { viewModel.onTabSelected(DebtType.OWED_TO_ME) },
                    text = {
                        Text(
                            text = "Owed to Me (${CurrencyFormatter.format(state.totalOwedToMe)})",
                            fontWeight = FontWeight.Bold,
                            color = if (state.selectedTab == DebtType.OWED_TO_ME) IncomeGreen else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }

            if (state.debts.isEmpty()) {
                FinEmptyState(
                    icon = Icons.Default.MoneyOff,
                    title = "No Records Found",
                    message = if (state.selectedTab == DebtType.OWED_BY_ME) {
                        "You don't have any recorded debts or loans you owe to others."
                    } else {
                        "You have not recorded any money lent to others."
                    },
                    actionButtonText = "Record Debt/Loan",
                    onActionClick = { viewModel.openAddDebt() }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.debts, key = { it.id }) { debt ->
                        DebtCardItem(
                            debt = debt,
                            onRecordPayment = { viewModel.openRecordPayment(debt) }
                        )
                    }
                }
            }
        }
    }

    if (state.isAddDebtOpen) {
        AddDebtDialog(
            defaultType = state.selectedTab,
            accounts = state.accounts,
            onDismiss = { viewModel.closeAddDebt() },
            onConfirm = { name, type, amt, rate, due, accId, notes ->
                viewModel.createDebt(name, type, amt, rate, due, accId, notes)
            }
        )
    }

    if (state.selectedDebtForPayment != null) {
        RecordPaymentDialog(
            debt = state.selectedDebtForPayment!!,
            accounts = state.accounts,
            onDismiss = { viewModel.closeRecordPayment() },
            onConfirm = { accId, amt, notes ->
                viewModel.recordPayment(state.selectedDebtForPayment!!.id, accId, amt, notes)
            }
        )
    }
}

@Composable
private fun DebtCardItem(
    debt: Debt,
    onRecordPayment: () -> Unit
) {
    val dateStr = if (debt.dueDate != null) {
        DateTimeFormatter.ofPattern("dd MMM yyyy")
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(debt.dueDate))
    } else "No Due Date"

    FinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = debt.personName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "Original: ${CurrencyFormatter.format(debt.originalAmount)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            val statusColor = when (debt.status) {
                DebtStatus.SETTLED -> IncomeGreen
                DebtStatus.PARTIALLY_PAID -> MaterialTheme.colorScheme.primary
                DebtStatus.ACTIVE -> ExpenseRed
                DebtStatus.OVERDUE -> MaterialTheme.colorScheme.error
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = statusColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = debt.status.name,
                    color = statusColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Outstanding Balance",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = CurrencyFormatter.format(debt.outstandingAmount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (debt.type == DebtType.OWED_BY_ME) ExpenseRed else IncomeGreen
                )
                Text(
                    text = "Due: $dateStr",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            if (debt.status != DebtStatus.SETTLED) {
                Button(
                    onClick = onRecordPayment,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Pay / Settle")
                }
            }
        }
    }
}

@Composable
private fun AddDebtDialog(
    defaultType: DebtType,
    accounts: List<Account>,
    onDismiss: () -> Unit,
    onConfirm: (String, DebtType, BigDecimal, BigDecimal, Long?, Long?, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(defaultType) }
    var amountStr by remember { mutableStateOf("") }
    var interestStr by remember { mutableStateOf("0") }
    var notes by remember { mutableStateOf("") }
    var selectedAccountId by remember { mutableStateOf<Long?>(accounts.firstOrNull()?.id) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Debt / Loan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == DebtType.OWED_BY_ME, onClick = { type = DebtType.OWED_BY_ME })
                    Text("Money I Owe", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = type == DebtType.OWED_TO_ME, onClick = { type = DebtType.OWED_TO_ME })
                    Text("Owed to Me", style = MaterialTheme.typography.bodyMedium)
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Person or Institution Name") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) amountStr = it },
                    label = { Text("Loan / Debt Amount") },
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
                    val interest = runCatching { BigDecimal(interestStr) }.getOrDefault(BigDecimal.ZERO)
                    if (name.isNotBlank() && amount != null && amount > BigDecimal.ZERO) {
                        onConfirm(name, type, amount, interest, null, selectedAccountId, notes)
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
private fun RecordPaymentDialog(
    debt: Debt,
    accounts: List<Account>,
    onDismiss: () -> Unit,
    onConfirm: (Long, BigDecimal, String) -> Unit
) {
    var selectedAccountId by remember { mutableStateOf(accounts.firstOrNull()?.id ?: 0L) }
    var amountStr by remember { mutableStateOf(debt.outstandingAmount.toPlainString()) }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Repayment for ${debt.personName}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Outstanding: ${CurrencyFormatter.format(debt.outstandingAmount)}", fontWeight = FontWeight.Bold)

                Text("Select Linked Account:", style = MaterialTheme.typography.labelSmall)
                accounts.forEach { acc ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        RadioButton(selected = selectedAccountId == acc.id, onClick = { selectedAccountId = acc.id })
                        Text("${acc.name} (${CurrencyFormatter.format(acc.currentBalance)})")
                    }
                }

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) amountStr = it },
                    label = { Text("Repayment Amount") },
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
                    if (amount != null && amount > BigDecimal.ZERO && selectedAccountId > 0) {
                        onConfirm(selectedAccountId, amount, notes)
                    }
                }
            ) {
                Text("Record Payment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

