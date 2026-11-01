package com.fintrack.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.AccountType
import java.math.BigDecimal

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
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
) {
    fun toDomain(): Account = Account(
        id = id,
        name = name,
        type = type,
        openingBalance = openingBalance,
        currentBalance = currentBalance,
        currency = currency,
        description = description,
        isArchived = isArchived,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(account: Account): AccountEntity = AccountEntity(
            id = account.id,
            name = account.name,
            type = account.type,
            openingBalance = account.openingBalance,
            currentBalance = account.currentBalance,
            currency = account.currency,
            description = account.description,
            isArchived = account.isArchived,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt
        )
    }
}

