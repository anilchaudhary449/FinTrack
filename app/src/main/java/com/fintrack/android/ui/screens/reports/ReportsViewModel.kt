package com.fintrack.android.ui.screens.reports

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.data.export.CsvExporter
import com.fintrack.android.data.export.FinancialStatementGenerator
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.domain.model.*
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.TransactionRepository
import com.fintrack.android.domain.usecase.GetDashboardSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class ReportsUiState(
    val timeRange: TimeRange = TimeRange.THIS_MONTH,
    val summary: FinancialSummary = FinancialSummary(
        totalBalance = BigDecimal.ZERO,
        totalIncome = BigDecimal.ZERO,
        totalExpenses = BigDecimal.ZERO,
        netSavings = BigDecimal.ZERO,
        budgetUtilizationPercentage = 0f,
        totalDebtsOwedByMe = BigDecimal.ZERO,
        totalDebtsOwedToMe = BigDecimal.ZERO,
        upcomingBillsCount = 0
    ),
    val categorySpending: List<CategorySpending> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val exportSuccessMessage: String? = null
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _timeRange = MutableStateFlow(TimeRange.THIS_MONTH)
    private val _exportMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ReportsUiState> = combine(
        _timeRange,
        accountRepository.getAllAccounts(),
        _exportMessage
    ) { range, accounts, message ->
        Pair(range, Pair(accounts, message))
    }.flatMapLatest { (range, pair) ->
        val (accounts, message) = pair
        getDashboardSummaryUseCase(range).combine(
            transactionRepository.getAllTransactions()
        ) { summary, allTxs ->
            val spending = calculateCategorySpending(allTxs, summary.totalExpenses)
            ReportsUiState(
                timeRange = range,
                summary = summary,
                categorySpending = spending,
                accounts = accounts,
                transactions = allTxs,
                exportSuccessMessage = message
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ReportsUiState()
    )

    fun onTimeRangeChange(range: TimeRange) {
        _timeRange.value = range
    }

    fun exportCsv(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val csvData = CsvExporter.exportTransactionsToCsv(_uiState.value.transactions)
                    val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
                    val file = File(exportDir, "FinTrack_Transactions_${System.currentTimeMillis()}.csv")
                    FileOutputStream(file).use { it.write(csvData.toByteArray(Charsets.UTF_8)) }

                    shareFile(context, file, "text/csv")
                } catch (e: Exception) {
                    _exportMessage.value = "CSV export failed: ${e.message}"
                }
            }
        }
    }

    fun exportStatementHtml(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val now = LocalDate.now()
                    val monthName = now.month.getDisplayName(TextStyle.FULL, Locale.US)
                    val html = FinancialStatementGenerator.generateHtmlMonthlyStatement(
                        monthName = monthName,
                        year = now.year,
                        summary = _uiState.value.summary,
                        transactions = _uiState.value.transactions.take(100)
                    )

                    val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
                    val file = File(exportDir, "FinTrack_Statement_${monthName}_${now.year}.html")
                    FileOutputStream(file).use { it.write(html.toByteArray(Charsets.UTF_8)) }

                    shareFile(context, file, "text/html")
                } catch (e: Exception) {
                    _exportMessage.value = "Statement export failed: ${e.message}"
                }
            }
        }
    }

    private fun shareFile(context: Context, file: File, mimeType: String) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Export FinTrack Statement")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun calculateCategorySpending(transactions: List<Transaction>, totalExpense: BigDecimal): List<CategorySpending> {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        if (expenses.isEmpty() || totalExpense.compareTo(BigDecimal.ZERO) <= 0) return emptyList()

        return expenses.groupBy { it.categoryId ?: 0L }
            .map { (catId, txs) ->
                val first = txs.first()
                val sum = txs.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }
                val percentage = sum.divide(totalExpense, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal(100)).toFloat()
                CategorySpending(
                    categoryId = catId,
                    categoryName = first.categoryName.ifEmpty { "General" },
                    colorHex = first.categoryColor.ifEmpty { "#EF4444" },
                    iconName = first.categoryIcon,
                    totalAmount = sum,
                    percentageOfTotal = percentage
                )
            }.sortedByDescending { it.totalAmount }
    }
}

