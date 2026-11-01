package com.fintrack.android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fintrack.android.data.local.converters.RoomTypeConverters
import com.fintrack.android.data.local.dao.*
import com.fintrack.android.data.local.entity.*
import com.fintrack.android.domain.model.AccountType
import com.fintrack.android.domain.model.CategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Database(
    entities = [
        AccountEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        SavingsGoalEntity::class,
        SavingsContributionEntity::class,
        DebtEntity::class,
        DebtPaymentEntity::class,
        RecurringTransactionEntity::class,
        AuditLogEntity::class,
        ReconciliationRecordEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsDao(): SavingsDao
    abstract fun debtDao(): DebtDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun reconciliationDao(): ReconciliationDao

    companion object {
        const val DATABASE_NAME = "fintrack.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, applicationScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabasePrepopulateCallback(applicationScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabasePrepopulateCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(database: AppDatabase) {
            val accountDao = database.accountDao()
            val categoryDao = database.categoryDao()

            // Seed default accounts
            if (accountDao.getAccountsCount() == 0) {
                accountDao.insertAll(
                    listOf(
                        AccountEntity(
                            name = "Cash Wallet",
                            type = AccountType.CASH_WALLET,
                            openingBalance = BigDecimal("5000.00"),
                            currentBalance = BigDecimal("5000.00"),
                            currency = "NPR",
                            description = "Physical cash on hand"
                        ),
                        AccountEntity(
                            name = "Bank Account",
                            type = AccountType.BANK_ACCOUNT,
                            openingBalance = BigDecimal("45000.00"),
                            currentBalance = BigDecimal("45000.00"),
                            currency = "NPR",
                            description = "Primary checking / salary account"
                        ),
                        AccountEntity(
                            name = "eSewa / Digital Wallet",
                            type = AccountType.DIGITAL_WALLET,
                            openingBalance = BigDecimal("3500.00"),
                            currentBalance = BigDecimal("3500.00"),
                            currency = "NPR",
                            description = "Daily utility & QR payments"
                        )
                    )
                )
            }

            // Seed default categories
            if (categoryDao.getCategoriesCount() == 0) {
                val categories = listOf(
                    // Income Categories
                    CategoryEntity(name = "Salary", type = CategoryType.INCOME, iconName = "payments", colorHex = "#10B981", isDefault = true),
                    CategoryEntity(name = "Business", type = CategoryType.INCOME, iconName = "store", colorHex = "#3B82F6", isDefault = true),
                    CategoryEntity(name = "Freelancing", type = CategoryType.INCOME, iconName = "laptop", colorHex = "#8B5CF6", isDefault = true),
                    CategoryEntity(name = "Investment Returns", type = CategoryType.INCOME, iconName = "trending_up", colorHex = "#059669", isDefault = true),
                    CategoryEntity(name = "Rental Income", type = CategoryType.INCOME, iconName = "home", colorHex = "#F59E0B", isDefault = true),
                    CategoryEntity(name = "Interest", type = CategoryType.INCOME, iconName = "savings", colorHex = "#EC4899", isDefault = true),
                    CategoryEntity(name = "Gifts", type = CategoryType.INCOME, iconName = "card_giftcard", colorHex = "#14B8A6", isDefault = true),
                    CategoryEntity(name = "Other Income", type = CategoryType.INCOME, iconName = "more_horiz", colorHex = "#6B7280", isDefault = true),

                    // Expense Categories
                    CategoryEntity(name = "Food & Groceries", type = CategoryType.EXPENSE, iconName = "shopping_cart", colorHex = "#EF4444", isDefault = true, isFavorite = true),
                    CategoryEntity(name = "Restaurants", type = CategoryType.EXPENSE, iconName = "restaurant", colorHex = "#F97316", isDefault = true),
                    CategoryEntity(name = "Shopping", type = CategoryType.EXPENSE, iconName = "shopping_bag", colorHex = "#EC4899", isDefault = true),
                    CategoryEntity(name = "Clothing", type = CategoryType.EXPENSE, iconName = "checkroom", colorHex = "#A855F7", isDefault = true),
                    CategoryEntity(name = "Healthcare", type = CategoryType.EXPENSE, iconName = "local_hospital", colorHex = "#DC2626", isDefault = true),
                    CategoryEntity(name = "Education", type = CategoryType.EXPENSE, iconName = "school", colorHex = "#2563EB", isDefault = true),
                    CategoryEntity(name = "Entertainment", type = CategoryType.EXPENSE, iconName = "movie", colorHex = "#7C3AED", isDefault = true),
                    CategoryEntity(name = "Personal Care", type = CategoryType.EXPENSE, iconName = "face", colorHex = "#DB2777", isDefault = true),
                    CategoryEntity(name = "Fuel & Transport", type = CategoryType.EXPENSE, iconName = "directions_car", colorHex = "#D97706", isDefault = true, isFavorite = true),
                    CategoryEntity(name = "Rent & Housing", type = CategoryType.EXPENSE, iconName = "house", colorHex = "#4B5563", isDefault = true),
                    CategoryEntity(name = "Electricity & Bills", type = CategoryType.EXPENSE, iconName = "bolt", colorHex = "#0284C7", isDefault = true),
                    CategoryEntity(name = "Internet & Mobile", type = CategoryType.EXPENSE, iconName = "wifi", colorHex = "#0D9488", isDefault = true),
                    CategoryEntity(name = "Loan Payments", type = CategoryType.EXPENSE, iconName = "receipt_long", colorHex = "#B91C1C", isDefault = true),
                    CategoryEntity(name = "Insurance", type = CategoryType.EXPENSE, iconName = "shield", colorHex = "#4338CA", isDefault = true),
                    CategoryEntity(name = "Bank Charges", type = CategoryType.EXPENSE, iconName = "account_balance", colorHex = "#64748B", isDefault = true),
                    CategoryEntity(name = "Investments", type = CategoryType.EXPENSE, iconName = "trending_up", colorHex = "#047857", isDefault = true),
                    CategoryEntity(name = "Taxes", type = CategoryType.EXPENSE, iconName = "request_quote", colorHex = "#991B1B", isDefault = true),
                    CategoryEntity(name = "Gifts & Donations", type = CategoryType.EXPENSE, iconName = "volunteer_activism", colorHex = "#D946EF", isDefault = true),
                    CategoryEntity(name = "Miscellaneous", type = CategoryType.EXPENSE, iconName = "category", colorHex = "#6B7280", isDefault = true)
                )
                categoryDao.insertAll(categories)
            }
        }
    }
}

