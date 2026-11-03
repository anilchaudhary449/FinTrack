package com.fintrack.android.ui.screens.networth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.domain.model.NetWorthSummary
import com.fintrack.android.domain.repository.ExchangeRateRepository
import com.fintrack.android.domain.usecase.CalculateNetWorthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class NetWorthViewModel @Inject constructor(
    calculateNetWorthUseCase: CalculateNetWorthUseCase,
    preferencesManager: UserPreferencesManager,
    exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {

    init {
        viewModelScope.launch { exchangeRateRepository.refreshIfStale() }
    }

    val netWorthSummary: StateFlow<NetWorthSummary> = preferencesManager.userPreferencesFlow
        .flatMapLatest { prefs -> calculateNetWorthUseCase(prefs.defaultCurrency) }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NetWorthSummary(
            totalAssets = BigDecimal.ZERO,
            totalLiabilities = BigDecimal.ZERO,
            netWorth = BigDecimal.ZERO,
            cashAndBankAssets = BigDecimal.ZERO,
            digitalWalletAssets = BigDecimal.ZERO,
            savingsAndInvestments = BigDecimal.ZERO,
            moneyLentAssets = BigDecimal.ZERO,
            loanLiabilities = BigDecimal.ZERO,
            creditCardLiabilities = BigDecimal.ZERO
        )
    )
}

