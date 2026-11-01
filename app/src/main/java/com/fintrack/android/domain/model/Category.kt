package com.fintrack.android.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val iconName: String = "category_default",
    val colorHex: String = "#006C4C",
    val parentCategoryId: Long? = null,
    val isDefault: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

