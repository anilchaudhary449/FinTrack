package com.fintrack.android.ui.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.model.Budget
import com.fintrack.android.domain.model.BudgetAlertStatus
import com.fintrack.android.domain.model.BudgetProgress
import com.fintrack.android.domain.model.Category
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinConfirmationDialog
import com.fintrack.android.ui.components.FinEmptyState
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import com.fintrack.android.ui.theme.WarningAmber
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onNavigateBack: () -> Unit,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var budgetToDelete by remember { mutableStateOf<Budget?>(null) }

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Budget Management",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.openAddDialog() }) {
                        Icon(Icons.Default.Add, contentDescription = "Create Budget")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Budget")
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
            // Overall Spending Overview
            item {
                FinCard {
                    Text(
                        text = "Monthly Spending vs Total Budgets",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Spent: ${CurrencyFormatter.format(state.totalSpent)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ExpenseRed
                        )
                        Text(
                            text = "Limit: ${CurrencyFormatter.format(state.totalBudgetLimit)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val overallProgress = if (state.totalBudgetLimit > BigDecimal.ZERO) {
                        (state.totalSpent.toFloat() / state.totalBudgetLimit.toFloat()).coerceIn(0f, 1f)
                    } else 0f
                    LinearProgressIndicator(
                        progress = { overallProgress },
                        modifier = Modifier.fillMaxWidth().height(10.dp),
                        color = if (overallProgress >= 1f) ExpenseRed else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            item {
                Text(
                    text = "Category Budgets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.budgetProgressList.isEmpty()) {
                item {
                    FinEmptyState(
                        icon = Icons.Default.PieChart,
                        title = "No Budgets Created",
                        message = "Set monthly spending limits for categories to prevent overspending and reach financial goals.",
                        actionButtonText = "Create Budget",
                        onActionClick = { viewModel.openAddDialog() }
                    )
                }
            } else {
                items(state.budgetProgressList, key = { it.budget.id }) { progress ->
                    BudgetItemCard(
                        progress = progress,
                        onDelete = { budgetToDelete = progress.budget }
                    )
                }
            }
        }
    }

    if (state.isAddDialogOpen) {
        AddBudgetDialog(
            categories = state.categories,
            onDismiss = { viewModel.closeAddDialog() },
            onConfirm = { name, catId, limit, threshold ->
                viewModel.createBudget(name, catId, limit, threshold)
            }
        )
    }

    if (budgetToDelete != null) {
        FinConfirmationDialog(
            title = "Delete Budget",
            message = "Are you sure you want to delete '${budgetToDelete?.name}'?",
            onConfirm = {
                budgetToDelete?.let { viewModel.deleteBudget(it) }
                budgetToDelete = null
            },
            onDismiss = { budgetToDelete = null }
        )
    }
}

@Composable
private fun BudgetItemCard(
    progress: BudgetProgress,
    onDelete: () -> Unit
) {
    val b = progress.budget
    val statusColor = when (progress.status) {
        BudgetAlertStatus.NORMAL -> IncomeGreen
        BudgetAlertStatus.WARNING -> WarningAmber
        BudgetAlertStatus.EXCEEDED -> ExpenseRed
    }
    val statusText = when (progress.status) {
        BudgetAlertStatus.NORMAL -> "Normal (${progress.percentageConsumed.toInt()}%)"
        BudgetAlertStatus.WARNING -> "Warning (${progress.percentageConsumed.toInt()}%)"
        BudgetAlertStatus.EXCEEDED -> "Exceeded (${progress.percentageConsumed.toInt()}%)"
    }

    FinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = b.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (b.categoryName != null) {
                    Text(
                        text = b.categoryName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { (progress.percentageConsumed / 100f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = statusColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Spent: ${CurrencyFormatter.format(progress.spentAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Remaining: ${CurrencyFormatter.format(progress.remainingAmount)}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (progress.status == BudgetAlertStatus.EXCEEDED) ExpenseRed else IncomeGreen
            )
        }
    }
}

@Composable
private fun AddBudgetDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onConfirm: (String, Long?, BigDecimal, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var limitStr by remember { mutableStateOf("") }
    var threshold by remember { mutableStateOf(80) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Budget") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Budget Title (e.g. Monthly Food)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = limitStr,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) limitStr = it },
                    label = { Text("Spending Limit Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                Text("Category (Optional):", style = MaterialTheme.typography.labelSmall)
                Column {
                    categories.take(6).forEach { cat ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedCategoryId == cat.id,
                                onClick = { selectedCategoryId = if (selectedCategoryId == cat.id) null else cat.id }
                            )
                            Text(cat.name)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val limit = runCatching { BigDecimal(limitStr) }.getOrNull()
                    if (name.isNotBlank() && limit != null && limit > BigDecimal.ZERO) {
                        onConfirm(name, selectedCategoryId, limit, threshold)
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

