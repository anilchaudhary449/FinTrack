package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.SavingsContribution
import com.fintrack.android.domain.model.SavingsGoal
import java.math.BigDecimal

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val targetAmount: BigDecimal,
    val currentSavedAmount: BigDecimal = BigDecimal.ZERO,
    val targetDate: Long,
    val isCompleted: Boolean = false,
    val iconName: String = "savings_default",
    val colorHex: String = "#10B981",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): SavingsGoal = SavingsGoal(
        id = id,
        title = title,
        description = description,
        targetAmount = targetAmount,
        currentSavedAmount = currentSavedAmount,
        targetDate = targetDate,
        isCompleted = isCompleted,
        iconName = iconName,
        colorHex = colorHex,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(goal: SavingsGoal): SavingsGoalEntity = SavingsGoalEntity(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            targetAmount = goal.targetAmount,
            currentSavedAmount = goal.currentSavedAmount,
            targetDate = goal.targetDate,
            isCompleted = goal.isCompleted,
            iconName = goal.iconName,
            colorHex = goal.colorHex,
            createdAt = goal.createdAt
        )
    }
}

@Entity(
    tableName = "savings_contributions",
    foreignKeys = [
        ForeignKey(
            entity = SavingsGoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["savingsGoalId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("savingsGoalId"),
        Index("accountId")
    ]
)
data class SavingsContributionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val savingsGoalId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val contributionDate: Long = System.currentTimeMillis(),
    val notes: String = ""
) {
    fun toDomain(): SavingsContribution = SavingsContribution(
        id = id,
        savingsGoalId = savingsGoalId,
        accountId = accountId,
        amount = amount,
        contributionDate = contributionDate,
        notes = notes
    )
}

