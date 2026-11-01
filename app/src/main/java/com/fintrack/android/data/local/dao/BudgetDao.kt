package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

data class BudgetWithCategory(
    @Embedded val budget: BudgetEntity,
    @ColumnInfo(name = "category_name") val categoryName: String?
)

@Dao
interface BudgetDao {

    @Query("""
        SELECT b.*, c.name as category_name 
        FROM budgets b 
        LEFT JOIN categories c ON b.categoryId = c.id
        WHERE b.month = :month AND b.year = :year
        ORDER BY b.id DESC
    """)
    fun getBudgetsForMonthFlow(month: Int, year: Int): Flow<List<BudgetWithCategory>>

    @Query("""
        SELECT b.*, c.name as category_name 
        FROM budgets b 
        LEFT JOIN categories c ON b.categoryId = c.id
        WHERE b.id = :id
    """)
    suspend fun getBudgetWithCategoryById(id: Long): BudgetWithCategory?

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
}

