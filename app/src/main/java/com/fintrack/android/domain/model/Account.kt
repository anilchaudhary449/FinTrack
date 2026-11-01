package com.fintrack.android.domain.model

import java.math.BigDecimal

data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val openingBalance: BigDecimal,
    val currentBalance: BigDecimal,
    val currency: String = "NPR",
    val description: String = "",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

