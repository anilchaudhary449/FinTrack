package com.fintrack.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.fintrack.android.data.preferences.ThemeMode
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.ui.navigation.FinTrackNavGraph
import com.fintrack.android.ui.theme.FinTrackTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val prefs by preferencesManager.userPreferencesFlow.collectAsState(
                initial = com.fintrack.android.data.preferences.UserPreferences()
            )

            val isDarkTheme = when (prefs.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            FinTrackTheme(darkTheme = isDarkTheme) {
                FinTrackNavGraph()
            }
        }
    }
}

