package com.fintrack.android.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.SpendingInsight
import com.fintrack.android.domain.usecase.GenerateSpendingInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    generateSpendingInsightsUseCase: GenerateSpendingInsightsUseCase
) : ViewModel() {

    val insights: StateFlow<List<SpendingInsight>> = generateSpendingInsightsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}

