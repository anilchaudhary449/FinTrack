package com.fintrack.android.domain.repository

import com.fintrack.android.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface AccountRepository {
    fun getAllAccounts(): Flow<List<Account>>
    suspend fun getAccountById(id: Long): Account?
    suspend fun createAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(account: Account)
}

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: CategoryType): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun createCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getRecentTransactions(limit: Int = 10): Flow<List<Transaction>>
    fun getTransactionsBetweenDates(startDate: Long, endDate: Long): Flow<List<Transaction>>
    suspend fun getTransactionsBetweenDatesDirect(startDate: Long, endDate: Long): List<Transaction>
    fun getTransactionsForAccount(accountId: Long): Flow<List<Transaction>>
    fun getTransactionsForCategory(categoryId: Long): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    fun searchTransactions(query: String): Flow<List<Transaction>>
    fun getFilteredTransactions(
        type: TransactionType?,
        accountId: Long?,
        categoryId: Long?,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Long)
}

interface BudgetRepository {
    fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>>
    suspend fun getBudgetById(id: Long): Budget?
    suspend fun createBudget(budget: Budget): Long
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
}

interface SavingsRepository {
    fun getAllSavingsGoals(): Flow<List<SavingsGoal>>
    suspend fun getSavingsGoalById(id: Long): SavingsGoal?
    fun getContributionsForGoal(goalId: Long): Flow<List<SavingsContribution>>
    suspend fun createSavingsGoal(goal: SavingsGoal): Long
    suspend fun updateSavingsGoal(goal: SavingsGoal)
    suspend fun deleteSavingsGoal(goal: SavingsGoal)
    suspend fun addContribution(contribution: SavingsContribution): Long
}

interface DebtRepository {
    fun getAllDebts(): Flow<List<Debt>>
    fun getDebtsByType(type: DebtType): Flow<List<Debt>>
    suspend fun getDebtById(id: Long): Debt?
    fun getPaymentsForDebt(debtId: Long): Flow<List<DebtPayment>>
    suspend fun createDebt(debt: Debt): Long
    suspend fun updateDebt(debt: Debt)
    suspend fun deleteDebt(debt: Debt)
    suspend fun recordDebtPayment(payment: DebtPayment): Long
}

interface RecurringRepository {
    fun getAllRecurring(): Flow<List<RecurringTransaction>>
    suspend fun getRecurringById(id: Long): RecurringTransaction?
    suspend fun createRecurring(recurring: RecurringTransaction): Long
    suspend fun updateRecurring(recurring: RecurringTransaction)
    suspend fun deleteRecurring(recurring: RecurringTransaction)
    suspend fun processDueRecurringTransactions(): Int
}

interface AuditRepository {
    fun getRecentAuditLogs(limit: Int = 100): Flow<List<AuditLog>>
    fun getAuditLogsForEntity(entityType: String, entityId: Long): Flow<List<AuditLog>>
    suspend fun recordAudit(log: AuditLog): Long
}

interface ReconciliationRepository {
    fun getReconciliationsForAccount(accountId: Long): Flow<List<ReconciliationRecord>>
    suspend fun reconcileAccount(
        accountId: Long,
        actualBalance: BigDecimal,
        notes: String
    ): Long
}

