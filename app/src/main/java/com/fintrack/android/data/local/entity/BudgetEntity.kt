package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.Budget
import java.math.BigDecimal

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("categoryId"),
        Index("month", "year")
    ]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: Long? = null,
    val amountLimit: BigDecimal,
    val warningThresholdPercentage: Int = 80,
    val month: Int,
    val year: Int,
    val startDate: Long,
    val endDate: Long,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(categoryName: String? = null): Budget = Budget(
        id = id,
        name = name,
        categoryId = categoryId,
        categoryName = categoryName,
        amountLimit = amountLimit,
        warningThresholdPercentage = warningThresholdPercentage,
        month = month,
        year = year,
        startDate = startDate,
        endDate = endDate,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(b: Budget): BudgetEntity = BudgetEntity(
            id = b.id,
            name = b.name,
            categoryId = b.categoryId,
            amountLimit = b.amountLimit,
            warningThresholdPercentage = b.warningThresholdPercentage,
            month = b.month,
            year = b.year,
            startDate = b.startDate,
            endDate = b.endDate,
            createdAt = b.createdAt
        )
    }
}

