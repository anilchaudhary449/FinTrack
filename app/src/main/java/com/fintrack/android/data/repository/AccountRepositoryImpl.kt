package com.fintrack.android.data.repository

import com.fintrack.android.data.local.AppDatabase
import com.fintrack.android.data.local.entity.AccountEntity
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : AccountRepository {

    private val accountDao = database.accountDao()
    private val auditDao = database.auditLogDao()

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccountsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(id: Long): Account? {
        return accountDao.getAccountById(id)?.toDomain()
    }

    override suspend fun createAccount(account: Account): Long {
        val entity = AccountEntity.fromDomain(account)
        val id = accountDao.insertAccount(entity)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "ACCOUNT",
                entityId = id,
                action = AuditAction.CREATED,
                description = "Created account '${account.name}' with balance ${account.currentBalance}"
            )
        )
        return id
    }

    override suspend fun updateAccount(account: Account) {
        val existing = accountDao.getAccountById(account.id) ?: return
        val updated = AccountEntity.fromDomain(account)
        accountDao.updateAccount(updated)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "ACCOUNT",
                entityId = account.id,
                action = AuditAction.UPDATED,
                description = "Updated account '${account.name}'",
                previousStateJson = existing.toString(),
                newStateJson = updated.toString()
            )
        )
    }

    override suspend fun deleteAccount(account: Account) {
        val entity = AccountEntity.fromDomain(account)
        accountDao.deleteAccount(entity)
        auditDao.insertAuditLog(
            AuditLogEntity(
                entityType = "ACCOUNT",
                entityId = account.id,
                action = AuditAction.DELETED,
                description = "Deleted account '${account.name}'"
            )
        )
    }
}

