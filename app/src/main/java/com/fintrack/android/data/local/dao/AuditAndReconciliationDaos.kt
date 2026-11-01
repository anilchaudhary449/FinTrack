package com.fintrack.android.data.local.dao

import androidx.room.*
import com.fintrack.android.data.local.entity.AuditLogEntity
import com.fintrack.android.data.local.entity.ReconciliationRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditLogDao {

    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC, id DESC LIMIT :limit")
    fun getRecentAuditLogsFlow(limit: Int = 100): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_logs WHERE entityType = :entityType AND entityId = :entityId ORDER BY timestamp DESC")
    fun getAuditLogsForEntityFlow(entityType: String, entityId: Long): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity): Long
}

@Dao
interface ReconciliationDao {

    @Query("SELECT * FROM reconciliation_records WHERE accountId = :accountId ORDER BY reconciliationDate DESC")
    fun getReconciliationsForAccountFlow(accountId: Long): Flow<List<ReconciliationRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReconciliationRecord(record: ReconciliationRecordEntity): Long
}

