package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.SavingsContributionEntity
import com.fintrack.android.data.local.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface SavingsDao {

    @Query("SELECT * FROM savings_goals ORDER BY isCompleted ASC, targetDate ASC")
    fun getAllSavingsGoalsFlow(): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goals WHERE id = :id")
    suspend fun getSavingsGoalById(id: Long): SavingsGoalEntity?

    @Query("SELECT * FROM savings_contributions WHERE savingsGoalId = :goalId ORDER BY contributionDate DESC")
    fun getContributionsForGoalFlow(goalId: Long): Flow<List<SavingsContributionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingsGoal(goal: SavingsGoalEntity): Long

    @Update
    suspend fun updateSavingsGoal(goal: SavingsGoalEntity)

    @Delete
    suspend fun deleteSavingsGoal(goal: SavingsGoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: SavingsContributionEntity): Long

    @Query("UPDATE savings_goals SET currentSavedAmount = currentSavedAmount + :amount WHERE id = :goalId")
    suspend fun addContributionAmount(goalId: Long, amount: BigDecimal)
}

