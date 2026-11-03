package com.fintrack.android.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.domain.util.BikramSambatConverter
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinEmptyState
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.screens.dashboard.TransactionItemRow
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetails: (Long) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val monthYearTitle = DateTimeFormatter.ofPattern("MMMM yyyy").format(state.currentYearMonth)
    val monthStartBs = BikramSambatConverter.toBikramSambatDate(state.currentYearMonth)
    val monthEndBs = BikramSambatConverter.toBikramSambatDate(
        state.currentYearMonth.withDayOfMonth(state.currentYearMonth.lengthOfMonth())
    )
    val bsMonthTitle = if (monthStartBs.month == monthEndBs.month && monthStartBs.year == monthEndBs.year) {
        "${monthStartBs.monthName} ${monthStartBs.year} BS"
    } else {
        "${monthStartBs.monthName} ${monthStartBs.year} - ${monthEndBs.monthName} ${monthEndBs.year} BS"
    }
    val selectedDateTitle = "${DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy 'AD'").format(state.selectedDate)} | ${BikramSambatConverter.format(BikramSambatConverter.toBikramSambatDate(state.selectedDate))}"

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Financial Calendar",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Month navigation header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.onPreviousMonth() }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$monthYearTitle AD",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = bsMonthTitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { viewModel.onNextMonth() }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { weekday ->
                    Text(
                        text = weekday,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Calendar days grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 12.dp)
            ) {
                val leadingDays = state.daysInMonth.firstOrNull()?.date?.dayOfWeek?.value?.minus(1) ?: 0
                items((0 until leadingDays).toList()) {
                    Spacer(modifier = Modifier.aspectRatio(1f))
                }
                items(state.daysInMonth) { day ->
                    val isSelected = day.date == state.selectedDate
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable { viewModel.onDateSelected(day.date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${day.dayOfMonth}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = "${BikramSambatConverter.toBikramSambatDate(day.date).day}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                if (day.totalExpense > BigDecimal.ZERO) {
                                    Box(modifier = Modifier.size(5.dp).background(ExpenseRed, CircleShape))
                                }
                                if (day.totalIncome > BigDecimal.ZERO) {
                                    Box(modifier = Modifier.size(5.dp).background(IncomeGreen, CircleShape))
                                }
                                if (day.hasUpcomingBill || day.hasLoanDue) {
                                    Box(modifier = Modifier.size(5.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Selected Day Summary
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedDateTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Income: ${CurrencyFormatter.format(state.selectedDateIncome)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = IncomeGreen
                    )
                    Text(
                        text = "Expense: ${CurrencyFormatter.format(state.selectedDateExpense)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ExpenseRed
                    )
                }
            }

            // Transactions on Selected Day
            if (state.selectedDateTransactions.isEmpty()) {
                FinEmptyState(
                    icon = Icons.Default.Event,
                    title = "No Transactions on this Day",
                    message = "No income or expenses were logged on the selected date."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.selectedDateTransactions, key = { it.id }) { tx ->
                        TransactionItemRow(
                            transaction = tx,
                            onClick = { onNavigateToTransactionDetails(tx.id) }
                        )
                    }
                }
            }
        }
    }
}

