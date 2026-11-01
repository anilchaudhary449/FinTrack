package com.fintrack.android.ui.screens.audit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.AuditLog
import com.fintrack.android.domain.repository.AuditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuditViewModel @Inject constructor(
    auditRepository: AuditRepository
) : ViewModel() {

    val auditLogs: StateFlow<List<AuditLog>> = auditRepository.getRecentAuditLogs(100).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}

