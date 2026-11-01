package com.fintrack.android.data.repository

import androidx.room.withTransaction
import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.data.local.entity.SavingsContributionEntity
import com.fintrack.android.data.local.entity.SavingsGoalEntity
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.model.SavingsContribution
import com.fintrack.android.domain.model.SavingsGoal
import com.fintrack.android.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavingsRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : SavingsRepository {

    private val savingsDao = database.savingsDao()
    private val accountDao = database.accountDao()
    private val auditDao = database.auditLogDao()

    override fun getAllSavingsGoals(): Flow<List<SavingsGoal>> {
        return savingsDao.getAllSavingsGoalsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getSavingsGoalById(id: Long): SavingsGoal? {
        return savingsDao.getSavingsGoalById(id)?.toDomain()
    }

    override fun getContributionsForGoal(goalId: Long): Flow<List<SavingsContribution>> {
        return savingsDao.getContributionsForGoalFlow(goalId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun createSavingsGoal(goal: SavingsGoal): Long {
        val entity = SavingsGoalEntity.fromDomain(goal)
        val id = savingsDao.insertSavingsGoal(entity)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "SAVINGS_GOAL",
                entityId = id,
                action = AuditAction.CREATED,
                description = "Created savings goal '${goal.title}' with target ${goal.targetAmount}"
            )
        )
        return id
    }

    override suspend fun updateSavingsGoal(goal: SavingsGoal) {
        val entity = SavingsGoalEntity.fromDomain(goal)
        savingsDao.updateSavingsGoal(entity)
    }

    override suspend fun deleteSavingsGoal(goal: SavingsGoal) {
        val entity = SavingsGoalEntity.fromDomain(goal)
        savingsDao.deleteSavingsGoal(entity)
    }

    override suspend fun addContribution(contribution: SavingsContribution): Long {
        return database.withTransaction {
            val contributionEntity = SavingsContributionEntity(
                savingsGoalId = contribution.savingsGoalId,
                accountId = contribution.accountId,
                amount = contribution.amount,
                contributionDate = contribution.contributionDate,
                notes = contribution.notes
            )
            val insertedId = savingsDao.insertContribution(contributionEntity)

            // Update goal currentSavedAmount
            val goal = savingsDao.getSavingsGoalById(contribution.savingsGoalId)
            if (goal != null) {
                val newSaved = goal.currentSavedAmount.add(contribution.amount)
                val isCompleted = newSaved >= goal.targetAmount
                savingsDao.updateSavingsGoal(goal.copy(currentSavedAmount = newSaved, isCompleted = isCompleted))
            }

            // Deduct from account balance
            val account = accountDao.getAccountById(contribution.accountId)
            if (account != null) {
                val newBal = account.currentBalance.subtract(contribution.amount)
                accountDao.updateAccountBalance(account.id, newBal)
            }

            auditDao.insertAuditLog(
                AuditLogEntity(
                    entityType = "SAVINGS_CONTRIBUTION",
                    entityId = insertedId,
                    action = AuditAction.CREATED,
                    description = "Contributed ${contribution.amount} towards savings goal #${contribution.savingsGoalId}"
                )
            )

            insertedId
        }
    }
}

