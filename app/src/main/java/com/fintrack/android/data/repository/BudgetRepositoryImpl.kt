package com.fintrack.android.data.repository

import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.data.local.entity.BudgetEntity
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.model.Budget
import com.fintrack.android.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : BudgetRepository {

    private val budgetDao = database.budgetDao()
    private val auditDao = database.auditLogDao()

    override fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> {
        return budgetDao.getBudgetsForMonthFlow(month, year).map { list ->
            list.map { it.budget.toDomain(it.categoryName) }
        }
    }

    override suspend fun getBudgetById(id: Long): Budget? {
        val budgetWithCategory = budgetDao.getBudgetWithCategoryById(id)
        return budgetWithCategory?.let { it.budget.toDomain(it.categoryName) }
    }

    override suspend fun createBudget(budget: Budget): Long {
        val entity = BudgetEntity.fromDomain(budget)
        val id = budgetDao.insertBudget(entity)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "BUDGET",
                entityId = id,
                action = AuditAction.CREATED,
                description = "Created budget '${budget.name}' with limit ${budget.amountLimit}"
            )
        )
        return id
    }

    override suspend fun updateBudget(budget: Budget) {
        val entity = BudgetEntity.fromDomain(budget)
        budgetDao.updateBudget(entity)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "BUDGET",
                entityId = budget.id,
                action = AuditAction.UPDATED,
                description = "Updated budget '${budget.name}' limit to ${budget.amountLimit}"
            )
        )
    }

    override suspend fun deleteBudget(budget: Budget) {
        val entity = BudgetEntity.fromDomain(budget)
        budgetDao.deleteBudget(entity)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "BUDGET",
                entityId = budget.id,
                action = AuditAction.DELETED,
                description = "Deleted budget '${budget.name}'"
            )
        )
    }
}

