package com.fintrack.android.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fintrack_prefs")

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class UserPreferences(
    val defaultCurrency: String = "NPR",
    val isBikramSambatEnabled: Boolean = false,
    val isPinEnabled: Boolean = false,
    val pinHash: String = "",
    val isBiometricEnabled: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isOnboardingCompleted: Boolean = false
)

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val DEFAULT_CURRENCY = stringPreferencesKey("default_currency")
        val BIKRAM_SAMBAT_ENABLED = booleanPreferencesKey("bikram_sambat_enabled")
        val PIN_ENABLED = booleanPreferencesKey("pin_enabled")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            UserPreferences(
                defaultCurrency = prefs[PreferencesKeys.DEFAULT_CURRENCY] ?: "NPR",
                isBikramSambatEnabled = prefs[PreferencesKeys.BIKRAM_SAMBAT_ENABLED] ?: false,
                isPinEnabled = prefs[PreferencesKeys.PIN_ENABLED] ?: false,
                pinHash = prefs[PreferencesKeys.PIN_HASH] ?: "",
                isBiometricEnabled = prefs[PreferencesKeys.BIOMETRIC_ENABLED] ?: false,
                themeMode = runCatching {
                    ThemeMode.valueOf(prefs[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name)
                }.getOrDefault(ThemeMode.SYSTEM),
                isOnboardingCompleted = prefs[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
            )
        }

    suspend fun setDefaultCurrency(currency: String) {
        context.dataStore.edit { it[PreferencesKeys.DEFAULT_CURRENCY] = currency }
    }

    suspend fun setBikramSambatEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.BIKRAM_SAMBAT_ENABLED] = enabled }
    }

    suspend fun setPin(pinHash: String) {
        context.dataStore.edit {
            it[PreferencesKeys.PIN_HASH] = pinHash
            it[PreferencesKeys.PIN_ENABLED] = pinHash.isNotEmpty()
        }
    }

    suspend fun disablePin() {
        context.dataStore.edit {
            it[PreferencesKeys.PIN_ENABLED] = false
            it[PreferencesKeys.PIN_HASH] = ""
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[PreferencesKeys.THEME_MODE] = mode.name }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.ONBOARDING_COMPLETED] = completed }
    }
}

