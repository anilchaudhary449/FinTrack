package com.fintrack.android.ui.screens.audit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.domain.model.AuditAction
import com.fintrack.android.domain.model.AuditLog
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinEmptyState
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import com.fintrack.android.ui.theme.TransferBlue
import com.fintrack.android.ui.theme.WarningAmber
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AuditScreen(
    onNavigateBack: () -> Unit,
    viewModel: AuditViewModel = hiltViewModel()
) {
    val logs by viewModel.auditLogs.collectAsState()

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Financial Audit Trail",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Immutable audit history of all recorded, edited, deleted, and reconciled transactions and accounts.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            if (logs.isEmpty()) {
                FinEmptyState(
                    icon = Icons.Default.History,
                    title = "No Audit Records Yet",
                    message = "Every financial change you make will be recorded here with an immutable timestamp."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(logs, key = { it.id }) { log ->
                        AuditLogItem(log)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditLogItem(log: AuditLog) {
    val dateStr = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a")
        .withZone(ZoneId.systemDefault())
        .format(Instant.ofEpochMilli(log.timestamp))

    val actionColor = when (log.action) {
        AuditAction.CREATED -> IncomeGreen
        AuditAction.UPDATED -> WarningAmber
        AuditAction.DELETED -> ExpenseRed
        AuditAction.RECONCILED -> TransferBlue
    }

    FinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = actionColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = log.action.name,
                    color = actionColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Text(
                text = "${log.entityType} #${log.entityId}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = log.description,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dateStr,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

