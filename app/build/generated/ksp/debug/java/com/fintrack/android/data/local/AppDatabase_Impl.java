package com.fintrack.android.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.fintrack.android.data.local.dao.AccountDao;
import com.fintrack.android.data.local.dao.AccountDao_Impl;
import com.fintrack.android.data.local.dao.AuditLogDao;
import com.fintrack.android.data.local.dao.AuditLogDao_Impl;
import com.fintrack.android.data.local.dao.BudgetDao;
import com.fintrack.android.data.local.dao.BudgetDao_Impl;
import com.fintrack.android.data.local.dao.CategoryDao;
import com.fintrack.android.data.local.dao.CategoryDao_Impl;
import com.fintrack.android.data.local.dao.DebtDao;
import com.fintrack.android.data.local.dao.DebtDao_Impl;
import com.fintrack.android.data.local.dao.ReconciliationDao;
import com.fintrack.android.data.local.dao.ReconciliationDao_Impl;
import com.fintrack.android.data.local.dao.RecurringTransactionDao;
import com.fintrack.android.data.local.dao.RecurringTransactionDao_Impl;
import com.fintrack.android.data.local.dao.SavingsDao;
import com.fintrack.android.data.local.dao.SavingsDao_Impl;
import com.fintrack.android.data.local.dao.TransactionDao;
import com.fintrack.android.data.local.dao.TransactionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AccountDao _accountDao;

  private volatile TransactionDao _transactionDao;

  private volatile CategoryDao _categoryDao;

  private volatile BudgetDao _budgetDao;

  private volatile SavingsDao _savingsDao;

  private volatile DebtDao _debtDao;

  private volatile RecurringTransactionDao _recurringTransactionDao;

  private volatile AuditLogDao _auditLogDao;

  private volatile ReconciliationDao _reconciliationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `openingBalance` TEXT NOT NULL, `currentBalance` TEXT NOT NULL, `currency` TEXT NOT NULL, `description` TEXT NOT NULL, `isArchived` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `amount` TEXT NOT NULL, `categoryId` INTEGER, `accountId` INTEGER NOT NULL, `toAccountId` INTEGER, `paymentMethod` TEXT NOT NULL, `transactionDate` INTEGER NOT NULL, `notes` TEXT NOT NULL, `receiptUri` TEXT, `currency` TEXT NOT NULL, `exchangeRate` TEXT NOT NULL, `recurringTransactionId` INTEGER, `debtId` INTEGER, `savingsGoalId` INTEGER, `isReconciled` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_accountId` ON `transactions` (`accountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_toAccountId` ON `transactions` (`toAccountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_categoryId` ON `transactions` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_transactionDate` ON `transactions` (`transactionDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_type` ON `transactions` (`type`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `iconName` TEXT NOT NULL, `colorHex` TEXT NOT NULL, `parentCategoryId` INTEGER, `isDefault` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `categoryId` INTEGER, `amountLimit` TEXT NOT NULL, `warningThresholdPercentage` INTEGER NOT NULL, `month` INTEGER NOT NULL, `year` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_categoryId` ON `budgets` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_month_year` ON `budgets` (`month`, `year`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `savings_goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `targetAmount` TEXT NOT NULL, `currentSavedAmount` TEXT NOT NULL, `targetDate` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `iconName` TEXT NOT NULL, `colorHex` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `savings_contributions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `savingsGoalId` INTEGER NOT NULL, `accountId` INTEGER NOT NULL, `amount` TEXT NOT NULL, `contributionDate` INTEGER NOT NULL, `notes` TEXT NOT NULL, FOREIGN KEY(`savingsGoalId`) REFERENCES `savings_goals`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_savings_contributions_savingsGoalId` ON `savings_contributions` (`savingsGoalId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_savings_contributions_accountId` ON `savings_contributions` (`accountId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `debts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `personName` TEXT NOT NULL, `type` TEXT NOT NULL, `originalAmount` TEXT NOT NULL, `outstandingAmount` TEXT NOT NULL, `interestRate` TEXT NOT NULL, `dueDate` INTEGER, `status` TEXT NOT NULL, `notes` TEXT NOT NULL, `contactNumber` TEXT NOT NULL, `accountId` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debts_accountId` ON `debts` (`accountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debts_type` ON `debts` (`type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debts_status` ON `debts` (`status`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `debt_payments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `debtId` INTEGER NOT NULL, `accountId` INTEGER NOT NULL, `amount` TEXT NOT NULL, `paymentDate` INTEGER NOT NULL, `notes` TEXT NOT NULL, FOREIGN KEY(`debtId`) REFERENCES `debts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debt_payments_debtId` ON `debt_payments` (`debtId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debt_payments_accountId` ON `debt_payments` (`accountId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recurring_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `type` TEXT NOT NULL, `amount` TEXT NOT NULL, `categoryId` INTEGER, `accountId` INTEGER NOT NULL, `paymentMethod` TEXT NOT NULL, `frequency` TEXT NOT NULL, `intervalValue` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER, `nextDueDate` INTEGER NOT NULL, `lastProcessedDate` INTEGER, `isActive` INTEGER NOT NULL, `autoCreate` INTEGER NOT NULL, `notes` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_accountId` ON `recurring_transactions` (`accountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_categoryId` ON `recurring_transactions` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_nextDueDate` ON `recurring_transactions` (`nextDueDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_isActive` ON `recurring_transactions` (`isActive`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `audit_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entityType` TEXT NOT NULL, `entityId` INTEGER NOT NULL, `action` TEXT NOT NULL, `description` TEXT NOT NULL, `previousStateJson` TEXT, `newStateJson` TEXT, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_audit_logs_entityType_entityId` ON `audit_logs` (`entityType`, `entityId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_audit_logs_timestamp` ON `audit_logs` (`timestamp`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reconciliation_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `accountId` INTEGER NOT NULL, `reconciliationDate` INTEGER NOT NULL, `appCalculatedBalance` TEXT NOT NULL, `actualStatementBalance` TEXT NOT NULL, `discrepancyAmount` TEXT NOT NULL, `adjustmentTransactionId` INTEGER, `notes` TEXT NOT NULL, FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reconciliation_records_accountId` ON `reconciliation_records` (`accountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reconciliation_records_reconciliationDate` ON `reconciliation_records` (`reconciliationDate`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'eebf2799214117c348766b5ec0ddc4dd')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `accounts`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `budgets`");
        db.execSQL("DROP TABLE IF EXISTS `savings_goals`");
        db.execSQL("DROP TABLE IF EXISTS `savings_contributions`");
        db.execSQL("DROP TABLE IF EXISTS `debts`");
        db.execSQL("DROP TABLE IF EXISTS `debt_payments`");
        db.execSQL("DROP TABLE IF EXISTS `recurring_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `audit_logs`");
        db.execSQL("DROP TABLE IF EXISTS `reconciliation_records`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAccounts = new HashMap<String, TableInfo.Column>(10);
        _columnsAccounts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("openingBalance", new TableInfo.Column("openingBalance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("currentBalance", new TableInfo.Column("currentBalance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccounts = new TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts);
        final TableInfo _existingAccounts = TableInfo.read(db, "accounts");
        if (!_infoAccounts.equals(_existingAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "accounts(com.fintrack.android.data.local.entity.AccountEntity).\n"
                  + " Expected:\n" + _infoAccounts + "\n"
                  + " Found:\n" + _existingAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(18);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("accountId", new TableInfo.Column("accountId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("toAccountId", new TableInfo.Column("toAccountId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("paymentMethod", new TableInfo.Column("paymentMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("transactionDate", new TableInfo.Column("transactionDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("receiptUri", new TableInfo.Column("receiptUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("exchangeRate", new TableInfo.Column("exchangeRate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("recurringTransactionId", new TableInfo.Column("recurringTransactionId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("debtId", new TableInfo.Column("debtId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("savingsGoalId", new TableInfo.Column("savingsGoalId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("isReconciled", new TableInfo.Column("isReconciled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("accounts", "RESTRICT", "NO ACTION", Arrays.asList("accountId"), Arrays.asList("id")));
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("categories", "SET NULL", "NO ACTION", Arrays.asList("categoryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(5);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_toAccountId", false, Arrays.asList("toAccountId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_transactionDate", false, Arrays.asList("transactionDate"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_type", false, Arrays.asList("type"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.fintrack.android.data.local.entity.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(9);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("iconName", new TableInfo.Column("iconName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("parentCategoryId", new TableInfo.Column("parentCategoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.fintrack.android.data.local.entity.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgets = new HashMap<String, TableInfo.Column>(10);
        _columnsBudgets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("amountLimit", new TableInfo.Column("amountLimit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("warningThresholdPercentage", new TableInfo.Column("warningThresholdPercentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("month", new TableInfo.Column("month", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("year", new TableInfo.Column("year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBudgets.add(new TableInfo.ForeignKey("categories", "SET NULL", "NO ACTION", Arrays.asList("categoryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBudgets = new HashSet<TableInfo.Index>(2);
        _indicesBudgets.add(new TableInfo.Index("index_budgets_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesBudgets.add(new TableInfo.Index("index_budgets_month_year", false, Arrays.asList("month", "year"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoBudgets = new TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets, _indicesBudgets);
        final TableInfo _existingBudgets = TableInfo.read(db, "budgets");
        if (!_infoBudgets.equals(_existingBudgets)) {
          return new RoomOpenHelper.ValidationResult(false, "budgets(com.fintrack.android.data.local.entity.BudgetEntity).\n"
                  + " Expected:\n" + _infoBudgets + "\n"
                  + " Found:\n" + _existingBudgets);
        }
        final HashMap<String, TableInfo.Column> _columnsSavingsGoals = new HashMap<String, TableInfo.Column>(10);
        _columnsSavingsGoals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("targetAmount", new TableInfo.Column("targetAmount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("currentSavedAmount", new TableInfo.Column("currentSavedAmount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("targetDate", new TableInfo.Column("targetDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("iconName", new TableInfo.Column("iconName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavingsGoals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSavingsGoals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSavingsGoals = new TableInfo("savings_goals", _columnsSavingsGoals, _foreignKeysSavingsGoals, _indicesSavingsGoals);
        final TableInfo _existingSavingsGoals = TableInfo.read(db, "savings_goals");
        if (!_infoSavingsGoals.equals(_existingSavingsGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "savings_goals(com.fintrack.android.data.local.entity.SavingsGoalEntity).\n"
                  + " Expected:\n" + _infoSavingsGoals + "\n"
                  + " Found:\n" + _existingSavingsGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsSavingsContributions = new HashMap<String, TableInfo.Column>(6);
        _columnsSavingsContributions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsContributions.put("savingsGoalId", new TableInfo.Column("savingsGoalId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsContributions.put("accountId", new TableInfo.Column("accountId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsContributions.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsContributions.put("contributionDate", new TableInfo.Column("contributionDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsContributions.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavingsContributions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysSavingsContributions.add(new TableInfo.ForeignKey("savings_goals", "CASCADE", "NO ACTION", Arrays.asList("savingsGoalId"), Arrays.asList("id")));
        _foreignKeysSavingsContributions.add(new TableInfo.ForeignKey("accounts", "RESTRICT", "NO ACTION", Arrays.asList("accountId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSavingsContributions = new HashSet<TableInfo.Index>(2);
        _indicesSavingsContributions.add(new TableInfo.Index("index_savings_contributions_savingsGoalId", false, Arrays.asList("savingsGoalId"), Arrays.asList("ASC")));
        _indicesSavingsContributions.add(new TableInfo.Index("index_savings_contributions_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        final TableInfo _infoSavingsContributions = new TableInfo("savings_contributions", _columnsSavingsContributions, _foreignKeysSavingsContributions, _indicesSavingsContributions);
        final TableInfo _existingSavingsContributions = TableInfo.read(db, "savings_contributions");
        if (!_infoSavingsContributions.equals(_existingSavingsContributions)) {
          return new RoomOpenHelper.ValidationResult(false, "savings_contributions(com.fintrack.android.data.local.entity.SavingsContributionEntity).\n"
                  + " Expected:\n" + _infoSavingsContributions + "\n"
                  + " Found:\n" + _existingSavingsContributions);
        }
        final HashMap<String, TableInfo.Column> _columnsDebts = new HashMap<String, TableInfo.Column>(13);
        _columnsDebts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("personName", new TableInfo.Column("personName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("originalAmount", new TableInfo.Column("originalAmount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("outstandingAmount", new TableInfo.Column("outstandingAmount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("interestRate", new TableInfo.Column("interestRate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("dueDate", new TableInfo.Column("dueDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("contactNumber", new TableInfo.Column("contactNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("accountId", new TableInfo.Column("accountId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDebts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysDebts.add(new TableInfo.ForeignKey("accounts", "SET NULL", "NO ACTION", Arrays.asList("accountId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDebts = new HashSet<TableInfo.Index>(3);
        _indicesDebts.add(new TableInfo.Index("index_debts_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        _indicesDebts.add(new TableInfo.Index("index_debts_type", false, Arrays.asList("type"), Arrays.asList("ASC")));
        _indicesDebts.add(new TableInfo.Index("index_debts_status", false, Arrays.asList("status"), Arrays.asList("ASC")));
        final TableInfo _infoDebts = new TableInfo("debts", _columnsDebts, _foreignKeysDebts, _indicesDebts);
        final TableInfo _existingDebts = TableInfo.read(db, "debts");
        if (!_infoDebts.equals(_existingDebts)) {
          return new RoomOpenHelper.ValidationResult(false, "debts(com.fintrack.android.data.local.entity.DebtEntity).\n"
                  + " Expected:\n" + _infoDebts + "\n"
                  + " Found:\n" + _existingDebts);
        }
        final HashMap<String, TableInfo.Column> _columnsDebtPayments = new HashMap<String, TableInfo.Column>(6);
        _columnsDebtPayments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("debtId", new TableInfo.Column("debtId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("accountId", new TableInfo.Column("accountId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("paymentDate", new TableInfo.Column("paymentDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDebtPayments = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDebtPayments.add(new TableInfo.ForeignKey("debts", "CASCADE", "NO ACTION", Arrays.asList("debtId"), Arrays.asList("id")));
        _foreignKeysDebtPayments.add(new TableInfo.ForeignKey("accounts", "RESTRICT", "NO ACTION", Arrays.asList("accountId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDebtPayments = new HashSet<TableInfo.Index>(2);
        _indicesDebtPayments.add(new TableInfo.Index("index_debt_payments_debtId", false, Arrays.asList("debtId"), Arrays.asList("ASC")));
        _indicesDebtPayments.add(new TableInfo.Index("index_debt_payments_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        final TableInfo _infoDebtPayments = new TableInfo("debt_payments", _columnsDebtPayments, _foreignKeysDebtPayments, _indicesDebtPayments);
        final TableInfo _existingDebtPayments = TableInfo.read(db, "debt_payments");
        if (!_infoDebtPayments.equals(_existingDebtPayments)) {
          return new RoomOpenHelper.ValidationResult(false, "debt_payments(com.fintrack.android.data.local.entity.DebtPaymentEntity).\n"
                  + " Expected:\n" + _infoDebtPayments + "\n"
                  + " Found:\n" + _existingDebtPayments);
        }
        final HashMap<String, TableInfo.Column> _columnsRecurringTransactions = new HashMap<String, TableInfo.Column>(17);
        _columnsRecurringTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("accountId", new TableInfo.Column("accountId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("paymentMethod", new TableInfo.Column("paymentMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("intervalValue", new TableInfo.Column("intervalValue", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("nextDueDate", new TableInfo.Column("nextDueDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("lastProcessedDate", new TableInfo.Column("lastProcessedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("autoCreate", new TableInfo.Column("autoCreate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecurringTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRecurringTransactions.add(new TableInfo.ForeignKey("accounts", "RESTRICT", "NO ACTION", Arrays.asList("accountId"), Arrays.asList("id")));
        _foreignKeysRecurringTransactions.add(new TableInfo.ForeignKey("categories", "SET NULL", "NO ACTION", Arrays.asList("categoryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRecurringTransactions = new HashSet<TableInfo.Index>(4);
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_nextDueDate", false, Arrays.asList("nextDueDate"), Arrays.asList("ASC")));
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_isActive", false, Arrays.asList("isActive"), Arrays.asList("ASC")));
        final TableInfo _infoRecurringTransactions = new TableInfo("recurring_transactions", _columnsRecurringTransactions, _foreignKeysRecurringTransactions, _indicesRecurringTransactions);
        final TableInfo _existingRecurringTransactions = TableInfo.read(db, "recurring_transactions");
        if (!_infoRecurringTransactions.equals(_existingRecurringTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "recurring_transactions(com.fintrack.android.data.local.entity.RecurringTransactionEntity).\n"
                  + " Expected:\n" + _infoRecurringTransactions + "\n"
                  + " Found:\n" + _existingRecurringTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsAuditLogs = new HashMap<String, TableInfo.Column>(8);
        _columnsAuditLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("entityType", new TableInfo.Column("entityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("entityId", new TableInfo.Column("entityId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("action", new TableInfo.Column("action", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("previousStateJson", new TableInfo.Column("previousStateJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("newStateJson", new TableInfo.Column("newStateJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAuditLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAuditLogs = new HashSet<TableInfo.Index>(2);
        _indicesAuditLogs.add(new TableInfo.Index("index_audit_logs_entityType_entityId", false, Arrays.asList("entityType", "entityId"), Arrays.asList("ASC", "ASC")));
        _indicesAuditLogs.add(new TableInfo.Index("index_audit_logs_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        final TableInfo _infoAuditLogs = new TableInfo("audit_logs", _columnsAuditLogs, _foreignKeysAuditLogs, _indicesAuditLogs);
        final TableInfo _existingAuditLogs = TableInfo.read(db, "audit_logs");
        if (!_infoAuditLogs.equals(_existingAuditLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "audit_logs(com.fintrack.android.data.local.entity.AuditLogEntity).\n"
                  + " Expected:\n" + _infoAuditLogs + "\n"
                  + " Found:\n" + _existingAuditLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsReconciliationRecords = new HashMap<String, TableInfo.Column>(8);
        _columnsReconciliationRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("accountId", new TableInfo.Column("accountId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("reconciliationDate", new TableInfo.Column("reconciliationDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("appCalculatedBalance", new TableInfo.Column("appCalculatedBalance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("actualStatementBalance", new TableInfo.Column("actualStatementBalance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("discrepancyAmount", new TableInfo.Column("discrepancyAmount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("adjustmentTransactionId", new TableInfo.Column("adjustmentTransactionId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReconciliationRecords.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReconciliationRecords = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReconciliationRecords.add(new TableInfo.ForeignKey("accounts", "CASCADE", "NO ACTION", Arrays.asList("accountId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReconciliationRecords = new HashSet<TableInfo.Index>(2);
        _indicesReconciliationRecords.add(new TableInfo.Index("index_reconciliation_records_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        _indicesReconciliationRecords.add(new TableInfo.Index("index_reconciliation_records_reconciliationDate", false, Arrays.asList("reconciliationDate"), Arrays.asList("ASC")));
        final TableInfo _infoReconciliationRecords = new TableInfo("reconciliation_records", _columnsReconciliationRecords, _foreignKeysReconciliationRecords, _indicesReconciliationRecords);
        final TableInfo _existingReconciliationRecords = TableInfo.read(db, "reconciliation_records");
        if (!_infoReconciliationRecords.equals(_existingReconciliationRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "reconciliation_records(com.fintrack.android.data.local.entity.ReconciliationRecordEntity).\n"
                  + " Expected:\n" + _infoReconciliationRecords + "\n"
                  + " Found:\n" + _existingReconciliationRecords);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "eebf2799214117c348766b5ec0ddc4dd", "f2fd486bfea030326af7d8b74f15622e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "accounts","transactions","categories","budgets","savings_goals","savings_contributions","debts","debt_payments","recurring_transactions","audit_logs","reconciliation_records");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `accounts`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `budgets`");
      _db.execSQL("DELETE FROM `savings_goals`");
      _db.execSQL("DELETE FROM `savings_contributions`");
      _db.execSQL("DELETE FROM `debts`");
      _db.execSQL("DELETE FROM `debt_payments`");
      _db.execSQL("DELETE FROM `recurring_transactions`");
      _db.execSQL("DELETE FROM `audit_logs`");
      _db.execSQL("DELETE FROM `reconciliation_records`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AccountDao.class, AccountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetDao.class, BudgetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SavingsDao.class, SavingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DebtDao.class, DebtDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecurringTransactionDao.class, RecurringTransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AuditLogDao.class, AuditLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReconciliationDao.class, ReconciliationDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AccountDao accountDao() {
    if (_accountDao != null) {
      return _accountDao;
    } else {
      synchronized(this) {
        if(_accountDao == null) {
          _accountDao = new AccountDao_Impl(this);
        }
        return _accountDao;
      }
    }
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public BudgetDao budgetDao() {
    if (_budgetDao != null) {
      return _budgetDao;
    } else {
      synchronized(this) {
        if(_budgetDao == null) {
          _budgetDao = new BudgetDao_Impl(this);
        }
        return _budgetDao;
      }
    }
  }

  @Override
  public SavingsDao savingsDao() {
    if (_savingsDao != null) {
      return _savingsDao;
    } else {
      synchronized(this) {
        if(_savingsDao == null) {
          _savingsDao = new SavingsDao_Impl(this);
        }
        return _savingsDao;
      }
    }
  }

  @Override
  public DebtDao debtDao() {
    if (_debtDao != null) {
      return _debtDao;
    } else {
      synchronized(this) {
        if(_debtDao == null) {
          _debtDao = new DebtDao_Impl(this);
        }
        return _debtDao;
      }
    }
  }

  @Override
  public RecurringTransactionDao recurringTransactionDao() {
    if (_recurringTransactionDao != null) {
      return _recurringTransactionDao;
    } else {
      synchronized(this) {
        if(_recurringTransactionDao == null) {
          _recurringTransactionDao = new RecurringTransactionDao_Impl(this);
        }
        return _recurringTransactionDao;
      }
    }
  }

  @Override
  public AuditLogDao auditLogDao() {
    if (_auditLogDao != null) {
      return _auditLogDao;
    } else {
      synchronized(this) {
        if(_auditLogDao == null) {
          _auditLogDao = new AuditLogDao_Impl(this);
        }
        return _auditLogDao;
      }
    }
  }

  @Override
  public ReconciliationDao reconciliationDao() {
    if (_reconciliationDao != null) {
      return _reconciliationDao;
    } else {
      synchronized(this) {
        if(_reconciliationDao == null) {
          _reconciliationDao = new ReconciliationDao_Impl(this);
        }
        return _reconciliationDao;
      }
    }
  }
}
