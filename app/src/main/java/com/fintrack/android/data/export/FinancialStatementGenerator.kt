package com.fintrack.android.data.export

import com.fintrack.android.domain.model.FinancialSummary
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.util.CurrencyFormatter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FinancialStatementGenerator {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        .withZone(ZoneId.systemDefault())

    fun generateHtmlMonthlyStatement(
        monthName: String,
        year: Int,
        summary: FinancialSummary,
        transactions: List<Transaction>
    ): String {
        val rows = StringBuilder()
        for (tx in transactions) {
            val dateStr = dateFormatter.format(Instant.ofEpochMilli(tx.transactionDate))
            val typeColor = when (tx.type) {
                TransactionType.INCOME -> "#10B981"
                TransactionType.EXPENSE -> "#EF4444"
                TransactionType.TRANSFER -> "#3B82F6"
                else -> "#6B7280"
            }
            val sign = if (tx.type == TransactionType.INCOME) "+" else if (tx.type == TransactionType.EXPENSE) "-" else ""

            rows.append(
                """
                <tr>
                    <td>$dateStr</td>
                    <td><strong>${tx.categoryName.ifEmpty { tx.type.name }}</strong></td>
                    <td>${tx.accountName}</td>
                    <td>${tx.paymentMethod.displayName}</td>
                    <td>${tx.notes}</td>
                    <td style="color: $typeColor; text-align: right; font-weight: bold;">
                        $sign${CurrencyFormatter.format(tx.amount, tx.currency)}
                    </td>
                </tr>
                """.trimIndent()
            )
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>FinTrack Financial Statement - $monthName $year</title>
            <style>
                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; margin: 40px; color: #1e293b; }
                .header { border-bottom: 2px solid #006C4C; padding-bottom: 20px; margin-bottom: 30px; display: flex; justify-content: space-between; align-items: flex-end; }
                .title { font-size: 28px; font-weight: bold; color: #006C4C; margin: 0; }
                .subtitle { color: #64748b; margin-top: 5px; }
                .summary-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 30px; }
                .card { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 16px; text-align: center; }
                .card-title { font-size: 13px; color: #64748b; text-transform: uppercase; margin-bottom: 8px; font-weight: 600; }
                .card-value { font-size: 20px; font-weight: bold; }
                .income { color: #10B981; }
                .expense { color: #EF4444; }
                .savings { color: #3B82F6; }
                .balance { color: #006C4C; }
                table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                th { background-color: #f1f5f9; text-align: left; padding: 12px; font-size: 13px; text-transform: uppercase; color: #475569; border-bottom: 1px solid #cbd5e1; }
                td { padding: 12px; border-bottom: 1px solid #e2e8f0; font-size: 14px; }
                .footer { margin-top: 40px; text-align: center; font-size: 12px; color: #94a3b8; border-top: 1px solid #e2e8f0; padding-top: 20px; }
            </style>
        </head>
        <body>
            <div class="header">
                <div>
                    <h1 class="title">FinTrack Statement</h1>
                    <div class="subtitle">Personal Finance &amp; Expense Report for $monthName $year</div>
                </div>
                <div style="text-align: right; font-size: 13px; color: #64748b;">
                    Generated on: ${dateFormatter.format(Instant.now())}
                </div>
            </div>

            <div class="summary-grid">
                <div class="card">
                    <div class="card-title">Total Income</div>
                    <div class="card-value income">${CurrencyFormatter.format(summary.totalIncome)}</div>
                </div>
                <div class="card">
                    <div class="card-title">Total Expense</div>
                    <div class="card-value expense">${CurrencyFormatter.format(summary.totalExpenses)}</div>
                </div>
                <div class="card">
                    <div class="card-title">Net Savings</div>
                    <div class="card-value savings">${CurrencyFormatter.format(summary.netSavings)}</div>
                </div>
                <div class="card">
                    <div class="card-title">Total Balance</div>
                    <div class="card-value balance">${CurrencyFormatter.format(summary.totalBalance)}</div>
                </div>
            </div>

            <h3 style="margin-bottom: 10px; color: #1e293b;">Detailed Transactions</h3>
            <table>
                <thead>
                    <tr>
                        <th>Date &amp; Time</th>
                        <th>Category</th>
                        <th>Account</th>
                        <th>Method</th>
                        <th>Notes</th>
                        <th style="text-align: right;">Amount</th>
                    </tr>
                </thead>
                <tbody>
                    $rows
                </tbody>
            </table>

            <div class="footer">
                FinTrack Personal Finance Management &bull; Accurate Offline Ledger &bull; All calculations verified
            </div>
        </body>
        </html>
        """.trimIndent()
    }
}

