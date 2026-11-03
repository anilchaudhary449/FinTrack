package com.fintrack.android.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.model.TimeRange
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.util.BikramSambatConverter
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.*
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import com.fintrack.android.ui.theme.TransferBlue
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    onNavigateToAddTransaction: (TransactionType) -> Unit,
    onNavigateToTransactionDetails: (Long) -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToNetWorth: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    adsEnabled: Boolean = false,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FinTopBar(
                title = "FinTrack",
                titleContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("FinTrack", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Personal finance", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToInsights) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = "Insights")
                    }
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Time Range Selector
            item {
                TimeRangeFilterRow(
                    selected = state.selectedTimeRange,
                    onSelected = { viewModel.onTimeRangeSelected(it) }
                )
            }

            // Total Balance & Quick Actions Card
            item {
                TotalBalanceCard(
                    balance = state.summary.totalBalance,
                    currency = state.defaultCurrency,
                    onAddIncome = { onNavigateToAddTransaction(TransactionType.INCOME) },
                    onAddExpense = { onNavigateToAddTransaction(TransactionType.EXPENSE) },
                    onTransfer = { onNavigateToAddTransaction(TransactionType.TRANSFER) }
                )
            }

            // Financial Summary Metrics (Income, Expense, Savings)
            item {
                FinancialSummaryRow(
                    income = state.summary.totalIncome,
                    expense = state.summary.totalExpenses,
                    savings = state.summary.netSavings,
                    currency = state.defaultCurrency
                )
            }

            // Net Worth Teaser Card
            item {
                NetWorthTeaserCard(
                    netWorth = state.netWorth.netWorth,
                    assets = state.netWorth.totalAssets,
                    liabilities = state.netWorth.totalLiabilities,
                    currency = state.defaultCurrency,
                    onClick = onNavigateToNetWorth
                )
            }

            // Income vs Expense Comparison Chart
            item {
                FinCard {
                    Text(
                        text = "Income vs Expense",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FinBarChart(
                        incomeAmount = state.summary.totalIncome,
                        expenseAmount = state.summary.totalExpenses,
                        currencyCode = state.defaultCurrency
                    )
                }
            }

            // Expense Breakdown Donut Chart
            if (state.categorySpending.isNotEmpty()) {
                item {
                    FinCard {
                        Text(
                            text = "Spending Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FinDonutChart(
                            items = state.categorySpending,
                            centerTitle = "Total Spent",
                            centerAmount = state.summary.totalExpenses
                        )
                    }
                }
            }

            // Debts & Bills Notice Bar
            if (state.summary.totalDebtsOwedByMe > BigDecimal.ZERO || state.summary.upcomingBillsCount > 0) {
                item {
                    DebtsAndBillsNotice(
                        debtsOwed = state.summary.totalDebtsOwedByMe,
                        upcomingBills = state.summary.upcomingBillsCount,
                        currency = state.defaultCurrency
                    )
                }
            }

            // Recent Transactions Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onNavigateToTransactions) {
                        Text(text = "See All", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Recent Transactions List
            if (state.recentTransactions.isEmpty()) {
                item {
                    FinEmptyState(
                        icon = Icons.Default.ReceiptLong,
                        title = "No Transactions",
                        message = "Start logging your income and expenses to maintain accurate records."
                    )
                }
            } else {
                items(state.recentTransactions) { tx ->
                    TransactionItemRow(
                        transaction = tx,
                        isBikramSambat = state.isBikramSambatEnabled,
                        onClick = { onNavigateToTransactionDetails(tx.id) }
                    )
                }
            }

            if (adsEnabled) {
                item {
                    FinCard {
                        FinTrackBannerAd()
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeRangeFilterRow(
    selected: TimeRange,
    onSelected: (TimeRange) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            TimeRange.TODAY to "Today",
            TimeRange.THIS_WEEK to "This Week",
            TimeRange.THIS_MONTH to "This Month",
            TimeRange.LAST_MONTH to "Last Month",
            TimeRange.THIS_YEAR to "This Year"
        ).forEach { (range, label) ->
            FilterChip(
                selected = selected == range,
                onClick = { onSelected(range) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun TotalBalanceCard(
    balance: BigDecimal,
    currency: String,
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit,
    onTransfer: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Total Available Balance",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = CurrencyFormatter.format(balance, currency),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Default.ArrowDownward,
                    label = "Income",
                    containerColor = IncomeGreen,
                    onClick = onAddIncome
                )
                QuickActionButton(
                    icon = Icons.Default.ArrowUpward,
                    label = "Expense",
                    containerColor = ExpenseRed,
                    onClick = onAddExpense
                )
                QuickActionButton(
                    icon = Icons.Default.SyncAlt,
                    label = "Transfer",
                    containerColor = TransferBlue,
                    onClick = onTransfer
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color.White.copy(alpha = 0.2f),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun FinancialSummaryRow(
    income: BigDecimal,
    expense: BigDecimal,
    savings: BigDecimal,
    currency: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FinCard(modifier = Modifier.weight(1f)) {
            Text(text = "Income", style = MaterialTheme.typography.labelSmall, color = IncomeGreen)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyFormatter.format(income, currency),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IncomeGreen
            )
        }
        FinCard(modifier = Modifier.weight(1f)) {
            Text(text = "Expense", style = MaterialTheme.typography.labelSmall, color = ExpenseRed)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyFormatter.format(expense, currency),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ExpenseRed
            )
        }
        FinCard(modifier = Modifier.weight(1f)) {
            Text(text = "Savings", style = MaterialTheme.typography.labelSmall, color = TransferBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyFormatter.format(savings, currency),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TransferBlue
            )
        }
    }
}

@Composable
private fun NetWorthTeaserCard(
    netWorth: BigDecimal,
    assets: BigDecimal,
    liabilities: BigDecimal,
    currency: String,
    onClick: () -> Unit
) {
    FinCard(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Current Net Worth",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = CurrencyFormatter.format(netWorth, currency),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Net Worth",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Assets: ${CurrencyFormatter.format(assets, currency)}",
                style = MaterialTheme.typography.labelSmall,
                color = IncomeGreen
            )
            Text(
                text = "Liabilities: ${CurrencyFormatter.format(liabilities, currency)}",
                style = MaterialTheme.typography.labelSmall,
                color = ExpenseRed
            )
        }
    }
}

@Composable
private fun DebtsAndBillsNotice(
    debtsOwed: BigDecimal,
    upcomingBills: Int,
    currency: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (debtsOwed > BigDecimal.ZERO) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                color = ExpenseRed.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ExpenseRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Owed Debt", style = MaterialTheme.typography.labelSmall, color = ExpenseRed)
                        Text(
                            text = CurrencyFormatter.format(debtsOwed, currency),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        if (upcomingBills > 0) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Upcoming Bills", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = "$upcomingBills due soon",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemRow(
    transaction: Transaction,
    isBikramSambat: Boolean = false,
    onClick: () -> Unit
) {
    val dateDisplay = if (isBikramSambat) {
        BikramSambatConverter.toBikramSambat(transaction.transactionDate)
    } else {
        DateTimeFormatter.ofPattern("dd MMM, hh:mm a")
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(transaction.transactionDate))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                val icon = when (transaction.type) {
                    TransactionType.INCOME -> Icons.Default.ArrowDownward
                    TransactionType.EXPENSE -> Icons.Default.ArrowUpward
                    TransactionType.TRANSFER -> Icons.Default.SyncAlt
                    else -> Icons.Default.Receipt
                }
                val iconColor = when (transaction.type) {
                    TransactionType.INCOME -> IncomeGreen
                    TransactionType.EXPENSE -> ExpenseRed
                    TransactionType.TRANSFER -> TransferBlue
                    else -> MaterialTheme.colorScheme.primary
                }

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = iconColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    val displayName = when (transaction.type) {
                        TransactionType.TRANSFER -> "Transfer to ${transaction.toAccountName.ifEmpty { "Account" }}"
                        else -> transaction.categoryName.ifEmpty { transaction.type.name }
                    }
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${transaction.accountName} • $dateDisplay",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            FinAmountDisplay(
                amount = transaction.amount,
                currencyCode = transaction.currency,
                type = transaction.type
            )
        }
    }
}

