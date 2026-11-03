package com.fintrack.android.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.android.data.export.JsonBackupManager
import com.fintrack.android.data.preferences.ThemeMode
import com.fintrack.android.data.preferences.UserPreferences
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.data.security.PinSecurity
import com.fintrack.android.domain.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val backupSuccessMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager,
    private val jsonBackupManager: JsonBackupManager,
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesManager.userPreferencesFlow,
        _message,
        _error
    ) { prefs, msg, err ->
        SettingsUiState(
            preferences = prefs,
            backupSuccessMessage = msg,
            errorMessage = err
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SettingsUiState()
    )

    fun onCurrencyChange(currency: String) {
        viewModelScope.launch {
            preferencesManager.setDefaultCurrency(currency)
            runCatching { exchangeRateRepository.refreshIfStale() }
        }
    }

    fun onBikramSambatToggle(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBikramSambatEnabled(enabled)
        }
    }

    fun onThemeChange(mode: ThemeMode) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(mode)
        }
    }

    fun onBiometricToggle(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBiometricEnabled(enabled)
        }
    }

    fun savePin(pin: String, confirmation: String) {
        if (!PinSecurity.isValidPin(pin)) {
            _error.value = "PIN must be 4 to 8 digits"
            return
        }
        if (pin != confirmation) {
            _error.value = "PINs do not match"
            return
        }

        viewModelScope.launch {
            val hashedPin = withContext(Dispatchers.Default) { PinSecurity.hash(pin) }
            preferencesManager.setPin(hashedPin)
            _message.value = "PIN unlock enabled"
            _error.value = null
        }
    }

    fun removePin() {
        viewModelScope.launch {
            preferencesManager.disablePin()
            _message.value = "PIN unlock disabled"
            _error.value = null
        }
    }

    fun createBackup(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val backupJson = jsonBackupManager.createFullBackup()
                    val backupDir = File(context.filesDir, "backups").apply { mkdirs() }
                    val file = File(backupDir, "FinTrack_Backup_${System.currentTimeMillis()}.json")
                    FileOutputStream(file).use { it.write(backupJson.toByteArray(Charsets.UTF_8)) }
                    _message.value = "Backup created successfully at: ${file.name}"
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = "Backup failed: ${e.message}"
                }
            }
        }
    }

    fun clearMessages() {
        _message.value = null
        _error.value = null
    }
}

