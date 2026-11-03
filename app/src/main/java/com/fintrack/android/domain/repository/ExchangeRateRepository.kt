package com.fintrack.android.domain.repository

import com.fintrack.android.domain.model.ExchangeRateSnapshot
import kotlinx.coroutines.flow.StateFlow

interface ExchangeRateRepository {
    val snapshot: StateFlow<ExchangeRateSnapshot?>

    suspend fun refreshIfStale()
}