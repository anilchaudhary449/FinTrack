package com.fintrack.android.ui.screens.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.domain.model.Account
import com.fintrack.android.domain.model.SavingsContribution
import com.fintrack.android.domain.model.SavingsGoal
import com.fintrack.android.domain.repository.AccountRepository
import com.fintrack.android.domain.repository.SavingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class SavingsUiState(
    val goals: List<SavingsGoal> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val totalSaved: BigDecimal = BigDecimal.ZERO,
    val totalTarget: BigDecimal = BigDecimal.ZERO,
    val isAddGoalOpen: Boolean = false,
    val selectedGoalForContribution: SavingsGoal? = null
)

@HiltViewModel
class SavingsViewModel @Inject constructor(
    private val savingsRepository: SavingsRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _isAddGoalOpen = MutableStateFlow(false)
    private val _selectedGoalForContribution = MutableStateFlow<SavingsGoal?>(null)

    val uiState: StateFlow<SavingsUiState> = combine(
        savingsRepository.getAllSavingsGoals(),
        accountRepository.getAllAccounts(),
        _isAddGoalOpen,
        _selectedGoalForContribution
    ) { goals, accounts, isAddOpen, selectedGoal ->
        val saved = goals.fold(BigDecimal.ZERO) { acc, g -> acc.add(g.currentSavedAmount) }
        val target = goals.fold(BigDecimal.ZERO) { acc, g -> acc.add(g.targetAmount) }

        SavingsUiState(
            goals = goals,
            accounts = accounts,
            totalSaved = saved,
            totalTarget = target,
            isAddGoalOpen = isAddOpen,
            selectedGoalForContribution = selectedGoal
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SavingsUiState()
    )

    fun openAddGoal() { _isAddGoalOpen.value = true }
    fun closeAddGoal() { _isAddGoalOpen.value = false }

    fun openContribute(goal: SavingsGoal) { _selectedGoalForContribution.value = goal }
    fun closeContribute() { _selectedGoalForContribution.value = null }

    fun createGoal(title: String, desc: String, targetAmount: BigDecimal, targetDate: Long) {
        viewModelScope.launch {
            savingsRepository.createSavingsGoal(
                SavingsGoal(
                    title = title,
                    description = desc,
                    targetAmount = targetAmount,
                    targetDate = targetDate
                )
            )
            closeAddGoal()
        }
    }

    fun addContribution(goalId: Long, accountId: Long, amount: BigDecimal, notes: String) {
        viewModelScope.launch {
            savingsRepository.addContribution(
                SavingsContribution(
                    savingsGoalId = goalId,
                    accountId = accountId,
                    amount = amount,
                    notes = notes
                )
            )
            closeContribute()
        }
    }
}

