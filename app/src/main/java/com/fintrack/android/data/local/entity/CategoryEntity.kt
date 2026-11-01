package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.Category
import com.fintrack.android.domain.model.CategoryType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val iconName: String = "category_default",
    val colorHex: String = "#006C4C",
    val parentCategoryId: Long? = null,
    val isDefault: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Category = Category(
        id = id,
        name = name,
        type = type,
        iconName = iconName,
        colorHex = colorHex,
        parentCategoryId = parentCategoryId,
        isDefault = isDefault,
        isFavorite = isFavorite,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(cat: Category): CategoryEntity = CategoryEntity(
            id = cat.id,
            name = cat.name,
            type = cat.type,
            iconName = cat.iconName,
            colorHex = cat.colorHex,
            parentCategoryId = cat.parentCategoryId,
            isDefault = cat.isDefault,
            isFavorite = cat.isFavorite,
            createdAt = cat.createdAt
        )
    }
}

