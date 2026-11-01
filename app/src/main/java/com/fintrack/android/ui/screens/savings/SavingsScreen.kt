package com.fintrack.android.ui.screens.savings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Savings
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
import com.fintrack.android.domain.model.SavingsGoal
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinEmptyState
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.IncomeGreen
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SavingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Savings Goals",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.openAddGoal() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Goal")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddGoal() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
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
            // Overall Savings Summary
            item {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Total Accumulated Savings",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CurrencyFormatter.format(state.totalSaved),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target Goal: ${CurrencyFormatter.format(state.totalTarget)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (state.goals.isEmpty()) {
                item {
                    FinEmptyState(
                        icon = Icons.Default.Savings,
                        title = "No Savings Goals",
                        message = "Create targets for emergency funds, vehicle purchases, tech gear, or travel.",
                        actionButtonText = "Create Goal",
                        onActionClick = { viewModel.openAddGoal() }
                    )
                }
            } else {
                items(state.goals, key = { it.id }) { goal ->
                    SavingsGoalCard(
                        goal = goal,
                        onContribute = { viewModel.openContribute(goal) }
                    )
                }
            }
        }
    }

    if (state.isAddGoalOpen) {
        AddSavingsGoalDialog(
            onDismiss = { viewModel.closeAddGoal() },
            onConfirm = { title, desc, target, date ->
                viewModel.createGoal(title, desc, target, date)
            }
        )
    }

    if (state.selectedGoalForContribution != null) {
        AddContributionDialog(
            goal = state.selectedGoalForContribution!!,
            accounts = state.accounts,
            onDismiss = { viewModel.closeContribute() },
            onConfirm = { accountId, amount, notes ->
                viewModel.addContribution(state.selectedGoalForContribution!!.id, accountId, amount, notes)
            }
        )
    }
}

@Composable
private fun SavingsGoalCard(
    goal: SavingsGoal,
    onContribute: () -> Unit
) {
    val dateStr = DateTimeFormatter.ofPattern("dd MMM yyyy")
        .withZone(ZoneId.systemDefault())
        .format(Instant.ofEpochMilli(goal.targetDate))

    FinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = goal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (goal.description.isNotBlank()) {
                    Text(
                        text = goal.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            if (goal.isCompleted) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = IncomeGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Completed!",
                        color = IncomeGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { (goal.progressPercentage / 100f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = IncomeGreen,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Saved: ${CurrencyFormatter.format(goal.currentSavedAmount)} (${goal.progressPercentage.toInt()}%)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = IncomeGreen
            )
            Text(
                text = "Target: ${CurrencyFormatter.format(goal.targetAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Target Date: $dateStr",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            if (!goal.isCompleted) {
                Button(
                    onClick = onContribute,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Contribute", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun AddSavingsGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, BigDecimal, Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var targetStr by remember { mutableStateOf("") }
    val oneYearAhead = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Savings Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title (e.g. Emergency Fund)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = targetStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) targetStr = it },
                    label = { Text("Target Amount") },
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
                    val target = runCatching { BigDecimal(targetStr) }.getOrNull()
                    if (title.isNotBlank() && target != null && target > BigDecimal.ZERO) {
                        onConfirm(title, desc, target, oneYearAhead)
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
private fun AddContributionDialog(
    goal: SavingsGoal,
    accounts: List<Account>,
    onDismiss: () -> Unit,
    onConfirm: (Long, BigDecimal, String) -> Unit
) {
    var selectedAccountId by remember { mutableStateOf(accounts.firstOrNull()?.id ?: 0L) }
    var amountStr by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Contribute to ${goal.title}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Select Account to Deduct Funds From:", style = MaterialTheme.typography.labelSmall)
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
                    label = { Text("Contribution Amount") },
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
                Text("Contribute")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

