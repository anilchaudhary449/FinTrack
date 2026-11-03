package com.fintrack.android.data.export

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.fintrack.android.domain.model.FinancialSummary
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.util.CurrencyFormatter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.io.File
import java.io.FileOutputStream

object FinancialStatementGenerator {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        .withZone(ZoneId.systemDefault())

    fun generatePdfMonthlyStatement(
        context: Context,
        monthName: String,
        year: Int,
        summary: FinancialSummary,
        transactions: List<Transaction>
    ): File {
        val document = PdfDocument()
        val pages = transactions.chunked(10).ifEmpty { listOf(emptyList()) }
        val pageWidth = 595
        val pageHeight = 842

        try {
            pages.forEachIndexed { index, pageTransactions ->
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, index + 1).create()
                val page = document.startPage(pageInfo)
                drawPage(
                    canvas = page.canvas,
                    pageWidth = pageWidth,
                    pageHeight = pageHeight,
                    monthName = monthName,
                    year = year,
                    summary = summary,
                    transactions = pageTransactions,
                    pageNumber = index + 1,
                    showSummary = index == 0
                )
                document.finishPage(page)
            }

            val exportDirectory = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(exportDirectory, "FinTrack_Statement_${monthName}_$year.pdf")
            FileOutputStream(file).use(document::writeTo)
            return file
        } finally {
            document.close()
        }
    }

    private fun drawPage(
        canvas: Canvas,
        pageWidth: Int,
        pageHeight: Int,
        monthName: String,
        year: Int,
        summary: FinancialSummary,
        transactions: List<Transaction>,
        pageNumber: Int,
        showSummary: Boolean
    ) {
        val margin = 40f
        val contentWidth = pageWidth - margin * 2
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(0, 108, 76)
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(30, 41, 59)
            textSize = 10f
        }
        val boldPaint = Paint(bodyPaint).apply { typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) }
        val secondaryPaint = Paint(bodyPaint).apply {
            color = android.graphics.Color.rgb(100, 116, 139)
            textSize = 9f
        }
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(203, 213, 225)
            strokeWidth = 1f
        }

        canvas.drawText("FinTrack Financial Statement", margin, 52f, titlePaint)
        canvas.drawText("Personal Finance Report - $monthName $year", margin, 72f, bodyPaint)
        canvas.drawText("Generated ${dateFormatter.format(Instant.now())}", margin, 91f, secondaryPaint)
        canvas.drawLine(margin, 108f, pageWidth - margin, 108f, linePaint)

        var y = 132f
        if (showSummary) {
            drawSummaryValue(canvas, "Total income", CurrencyFormatter.format(summary.totalIncome), margin, y, bodyPaint, boldPaint, secondaryPaint)
            drawSummaryValue(canvas, "Total expenses", CurrencyFormatter.format(summary.totalExpenses), margin + contentWidth / 2, y, bodyPaint, boldPaint, secondaryPaint)
            y += 43f
            drawSummaryValue(canvas, "Net savings", CurrencyFormatter.format(summary.netSavings), margin, y, bodyPaint, boldPaint, secondaryPaint)
            drawSummaryValue(canvas, "Total balance", CurrencyFormatter.format(summary.totalBalance), margin + contentWidth / 2, y, bodyPaint, boldPaint, secondaryPaint)
            y += 38f
        }

        canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
        y += 23f
        canvas.drawText("Transactions", margin, y, boldPaint)
        y += 17f

        if (transactions.isEmpty()) {
            canvas.drawText("No transactions recorded for this statement.", margin, y + 10f, secondaryPaint)
        } else {
            transactions.forEach { transaction ->
                val date = dateFormatter.format(Instant.ofEpochMilli(transaction.transactionDate))
                val amount = CurrencyFormatter.format(transaction.amount, transaction.currency)
                val amountWidth = boldPaint.measureText(amount)
                val leftWidth = contentWidth - amountWidth - 12f
                drawFittedText(canvas, date, margin, y, leftWidth, secondaryPaint)
                canvas.drawText(amount, pageWidth - margin - amountWidth, y, boldPaint)
                y += 14f
                val category = transaction.categoryName.ifEmpty { transaction.type.name }
                drawFittedText(canvas, "$category | ${transaction.accountName}", margin, y, contentWidth, bodyPaint)
                y += 14f
                val details = listOf(transaction.paymentMethod.displayName, transaction.notes)
                    .filter(String::isNotBlank)
                    .joinToString(" | ")
                if (details.isNotEmpty()) {
                    drawFittedText(canvas, details, margin, y, contentWidth, secondaryPaint)
                }
                y += 13f
                canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
                y += 8f
            }
        }

        canvas.drawLine(margin, pageHeight - 38f, pageWidth - margin, pageHeight - 38f, linePaint)
        canvas.drawText("FinTrack - Page $pageNumber", margin, pageHeight - 22f, secondaryPaint)
    }

    private fun drawSummaryValue(
        canvas: Canvas,
        label: String,
        value: String,
        x: Float,
        y: Float,
        bodyPaint: Paint,
        valuePaint: Paint,
        labelPaint: Paint
    ) {
        canvas.drawText(label, x, y, labelPaint)
        drawFittedText(canvas, value, x, y + 16f, 245f, valuePaint)
    }

    private fun drawFittedText(canvas: Canvas, text: String, x: Float, y: Float, width: Float, paint: Paint) {
        if (paint.measureText(text) <= width) {
            canvas.drawText(text, x, y, paint)
            return
        }

        var end = text.length
        while (end > 0 && paint.measureText("${text.substring(0, end)}...") > width) {
            end--
        }
        canvas.drawText("${text.substring(0, end).trimEnd()}...", x, y, paint)
    }
}

