package com.fintrack.android.data.export

import com.fintrack.android.domain.model.Transaction
import java.io.StringWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object CsvExporter {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    fun exportTransactionsToCsv(transactions: List<Transaction>): String {
        val writer = StringWriter()
        // Header
        writer.append("ID,Date,Type,Category,Account,ToAccount,Amount,Currency,PaymentMethod,Notes,IsReconciled\n")

        for (tx in transactions) {
            val dateStr = dateFormatter.format(Instant.ofEpochMilli(tx.transactionDate))
            val safeCategory = escapeCsv(tx.categoryName)
            val safeAccount = escapeCsv(tx.accountName)
            val safeToAccount = escapeCsv(tx.toAccountName)
            val safeNotes = escapeCsv(tx.notes)

            writer.append("${tx.id},")
            writer.append("$dateStr,")
            writer.append("${tx.type.name},")
            writer.append("$safeCategory,")
            writer.append("$safeAccount,")
            writer.append("$safeToAccount,")
            writer.append("${tx.amount.toPlainString()},")
            writer.append("${tx.currency},")
            writer.append("${tx.paymentMethod.displayName},")
            writer.append("$safeNotes,")
            writer.append("${tx.isReconciled}\n")
        }

        return writer.toString()
    }

    private fun escapeCsv(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}

