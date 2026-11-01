package com.fintrack.android.ui.screens.reports

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.model.TimeRange
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinBarChart
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinDonutChart
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import com.fintrack.android.ui.theme.TransferBlue
import java.math.BigDecimal

@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Financial Analytics & Reports",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Time range selector
            item {
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        TimeRange.THIS_WEEK to "This Week",
                        TimeRange.THIS_MONTH to "This Month",
                        TimeRange.LAST_MONTH to "Last Month",
                        TimeRange.THIS_YEAR to "This Year"
                    ).forEach { (range, label) ->
                        FilterChip(
                            selected = state.timeRange == range,
                            onClick = { viewModel.onTimeRangeChange(range) },
                            label = { Text(label) }
                        )
                    }
                }
            }

            // Cash Flow Summary Card
            item {
                FinCard {
                    Text(
                        text = "Net Cash Flow Analysis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Income", style = MaterialTheme.typography.labelSmall, color = IncomeGreen)
                            Text(
                                text = CurrencyFormatter.format(state.summary.totalIncome),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = IncomeGreen
                            )
                        }
                        Column {
                            Text("Total Expense", style = MaterialTheme.typography.labelSmall, color = ExpenseRed)
                            Text(
                                text = CurrencyFormatter.format(state.summary.totalExpenses),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ExpenseRed
                            )
                        }
                        Column {
                            Text("Net Savings", style = MaterialTheme.typography.labelSmall, color = TransferBlue)
                            Text(
                                text = CurrencyFormatter.format(state.summary.netSavings),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TransferBlue
                            )
                        }
                    }
                }
            }

            // Bar Chart: Income vs Expense
            item {
                FinCard {
                    Text(
                        text = "Cash Inflow vs Outflow",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FinBarChart(
                        incomeAmount = state.summary.totalIncome,
                        expenseAmount = state.summary.totalExpenses
                    )
                }
            }

            // Donut Chart: Category Spending
            if (state.categorySpending.isNotEmpty()) {
                item {
                    FinCard {
                        Text(
                            text = "Expenses by Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FinDonutChart(
                            items = state.categorySpending,
                            centerTitle = "Total Outflow",
                            centerAmount = state.summary.totalExpenses
                        )
                    }
                }
            }

            // Account Balances List
            item {
                FinCard {
                    Text(
                        text = "Account Holdings Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    state.accounts.forEach { acc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(acc.name, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = CurrencyFormatter.format(acc.currentBalance, acc.currency),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Export Actions
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Statement & Data Export",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.exportCsv(context) },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(Icons.Default.TableChart, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export CSV")
                        }

                        Button(
                            onClick = { viewModel.exportStatementHtml(context) },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PDF Statement")
                        }
                    }
                }
            }
        }
    }
}

