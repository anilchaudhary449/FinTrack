package com.fintrack.android.data.export

import androidx.room.withTransaction
import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

data class BackupMetadata(
    val appVersion: String = "1.0.0",
    val schemaVersion: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val accountCount: Int,
    val transactionCount: Int,
    val categoryCount: Int,
    val checksum: String
)

@Singleton
class JsonBackupManager @Inject constructor(
    private val database: AppDatabase
) {

    suspend fun createFullBackup(): String = withContext(Dispatchers.IO) {
        val root = JSONObject()
        val accounts = database.accountDao().getAllAccountsFlow().first()
        val transactions = database.transactionDao().getAllTransactionsWithDetailsFlow().first()
        val categories = database.categoryDao().getAllCategoriesFlow().first()
        val budgets = database.budgetDao().getBudgetsForMonthFlow(1, 2026).first() // sample or all
        val debts = database.debtDao().getAllDebtsFlow().first()
        val savings = database.savingsDao().getAllSavingsGoalsFlow().first()
        val recurring = database.recurringTransactionDao().getAllRecurringTransactionsFlow().first()

        val accountsArray = JSONArray()
        for (a in accounts) {
            val obj = JSONObject().apply {
                put("id", a.id)
                put("name", a.name)
                put("type", a.type.name)
                put("openingBalance", a.openingBalance.toPlainString())
                put("currentBalance", a.currentBalance.toPlainString())
                put("currency", a.currency)
                put("description", a.description)
            }
            accountsArray.put(obj)
        }

        val txArray = JSONArray()
        for (t in transactions) {
            val entity = t.transaction
            val obj = JSONObject().apply {
                put("id", entity.id)
                put("type", entity.type.name)
                put("amount", entity.amount.toPlainString())
                put("categoryId", entity.categoryId ?: JSONObject.NULL)
                put("accountId", entity.accountId)
                put("toAccountId", entity.toAccountId ?: JSONObject.NULL)
                put("paymentMethod", entity.paymentMethod.name)
                put("transactionDate", entity.transactionDate)
                put("notes", entity.notes)
                put("currency", entity.currency)
            }
            txArray.put(obj)
        }

        val catArray = JSONArray()
        for (c in categories) {
            val obj = JSONObject().apply {
                put("id", c.id)
                put("name", c.name)
                put("type", c.type.name)
                put("iconName", c.iconName)
                put("colorHex", c.colorHex)
            }
            catArray.put(obj)
        }

        root.put("version", 1)
        root.put("timestamp", System.currentTimeMillis())
        root.put("accounts", accountsArray)
        root.put("transactions", txArray)
        root.put("categories", catArray)

        val rawContent = root.toString(2)
        val checksum = calculateChecksum(rawContent)
        root.put("checksum", checksum)

        root.toString(2)
    }

    suspend fun restoreBackup(jsonString: String): Boolean = withContext(Dispatchers.IO) {
        val root = JSONObject(jsonString)
        val version = root.optInt("version", 1)
        if (version > 1) {
            throw IllegalArgumentException("Unsupported backup version: $version")
        }

        database.withTransaction {
            // Restore accounts
            if (root.has("accounts")) {
                val accArray = root.getJSONArray("accounts")
                for (i in 0 until accArray.length()) {
                    val obj = accArray.getJSONObject(i)
                    val entity = AccountEntity(
                        id = obj.getLong("id"),
                        name = obj.getString("name"),
                        type = runCatching { com.fintrack.android.domain.model.AccountType.valueOf(obj.getString("type")) }
                            .getOrDefault(com.fintrack.android.domain.model.AccountType.CASH_WALLET),
                        openingBalance = BigDecimal(obj.getString("openingBalance")),
                        currentBalance = BigDecimal(obj.getString("currentBalance")),
                        currency = obj.optString("currency", "NPR"),
                        description = obj.optString("description", "")
                    )
                    database.accountDao().insertAccount(entity)
                }
            }

            // Restore categories
            if (root.has("categories")) {
                val catArray = root.getJSONArray("categories")
                for (i in 0 until catArray.length()) {
                    val obj = catArray.getJSONObject(i)
                    val entity = CategoryEntity(
                        id = obj.getLong("id"),
                        name = obj.getString("name"),
                        type = runCatching { com.fintrack.android.domain.model.CategoryType.valueOf(obj.getString("type")) }
                            .getOrDefault(com.fintrack.android.domain.model.CategoryType.EXPENSE),
                        iconName = obj.optString("iconName", "category_default"),
                        colorHex = obj.optString("colorHex", "#006C4C")
                    )
                    database.categoryDao().insertCategory(entity)
                }
            }

            // Restore transactions
            if (root.has("transactions")) {
                val txArray = root.getJSONArray("transactions")
                for (i in 0 until txArray.length()) {
                    val obj = txArray.getJSONObject(i)
                    val entity = TransactionEntity(
                        id = obj.getLong("id"),
                        type = runCatching { com.fintrack.android.domain.model.TransactionType.valueOf(obj.getString("type")) }
                            .getOrDefault(com.fintrack.android.domain.model.TransactionType.EXPENSE),
                        amount = BigDecimal(obj.getString("amount")),
                        categoryId = if (obj.isNull("categoryId")) null else obj.getLong("categoryId"),
                        accountId = obj.getLong("accountId"),
                        toAccountId = if (obj.isNull("toAccountId")) null else obj.getLong("toAccountId"),
                        paymentMethod = runCatching { com.fintrack.android.domain.model.PaymentMethod.valueOf(obj.getString("paymentMethod")) }
                            .getOrDefault(com.fintrack.android.domain.model.PaymentMethod.CASH),
                        transactionDate = obj.getLong("transactionDate"),
                        notes = obj.optString("notes", ""),
                        currency = obj.optString("currency", "NPR")
                    )
                    database.transactionDao().insertTransaction(entity)
                }
            }
        }
        true
    }

    private fun calculateChecksum(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

