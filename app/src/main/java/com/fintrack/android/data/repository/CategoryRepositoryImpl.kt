package com.fintrack.android.data.repository

import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.CategoryEntity
import com.fintrack.android.domain.model.Category
import com.fintrack.android.domain.model.CategoryType
import com.fintrack.android.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : CategoryRepository {

    private val categoryDao = database.categoryDao()

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategoriesFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getCategoriesByType(type: CategoryType): Flow<List<Category>> {
        return categoryDao.getCategoriesByTypeFlow(type).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override suspend fun createCategory(category: Category): Long {
        val entity = CategoryEntity.fromDomain(category)
        return categoryDao.insertCategory(entity)
    }

    override suspend fun updateCategory(category: Category) {
        val entity = CategoryEntity.fromDomain(category)
        categoryDao.updateCategory(entity)
    }

    override suspend fun deleteCategory(category: Category) {
        val entity = CategoryEntity.fromDomain(category)
        categoryDao.deleteCategory(entity)
    }
}

