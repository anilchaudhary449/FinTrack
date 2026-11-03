package com.fintrack.android

import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.biometric.BiometricManager
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import androidx.compose.foundation.isSystemInDarkTheme
import com.fintrack.android.data.security.PinSecurity
import com.fintrack.android.data.preferences.ThemeMode
import com.fintrack.android.data.preferences.UserPreferencesManager
import com.fintrack.android.ui.navigation.FinTrackNavGraph
import com.fintrack.android.ui.theme.FinTrackTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val adsMayLoad = mutableStateOf(false)
    private var adsInitialized = false

    @Inject
    lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val prefs by preferencesManager.userPreferencesFlow.collectAsState(
                initial = null
            )
            var unlocked by rememberSaveable { mutableStateOf(false) }
            var pinEntry by rememberSaveable { mutableStateOf("") }
            var pinError by rememberSaveable { mutableStateOf(false) }
            val lifecycleOwner = LocalLifecycleOwner.current

            val securityEnabled = prefs?.let { it.isPinEnabled || it.isBiometricEnabled } == true
            LaunchedEffect(prefs?.isPinEnabled, prefs?.isBiometricEnabled, prefs?.pinHash) {
                unlocked = !securityEnabled
            }
            androidx.compose.runtime.DisposableEffect(lifecycleOwner, securityEnabled) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_STOP && securityEnabled) unlocked = false
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }

            FinTrackTheme(darkTheme = prefs?.themeMode?.let { mode ->
                when (mode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }
            } ?: isSystemInDarkTheme()) {
                if (prefs == null) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        CircularProgressIndicator()
                    }
                } else if (securityEnabled && !unlocked) {
                    AppLockScreen(
                        showPin = prefs!!.isPinEnabled,
                        showBiometric = prefs!!.isBiometricEnabled,
                        pin = pinEntry,
                        pinError = pinError,
                        onPinChange = { pinEntry = it.filter(Char::isDigit).take(8) },
                        onSubmitPin = {
                            if (PinSecurity.matches(pinEntry, prefs!!.pinHash)) {
                                unlocked = true
                                pinEntry = ""
                                pinError = false
                            } else {
                                pinEntry = ""
                                pinError = true
                            }
                        },
                        onBiometric = { authenticateBiometric { unlocked = true; pinError = false } }
                    )
                } else {
                    FinTrackNavGraph(adsEnabled = adsMayLoad.value)
                }
            }
        }
        requestAdConsent()
    }

    private fun requestAdConsent() {
        val consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val parameters = ConsentRequestParameters.Builder().build()
        consentInformation.requestConsentInfoUpdate(
            this,
            parameters,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) {
                    initializeAdsIfAllowed(consentInformation)
                }
            },
            {
                initializeAdsIfAllowed(consentInformation)
            }
        )
        initializeAdsIfAllowed(consentInformation)
    }

    private fun initializeAdsIfAllowed(consentInformation: ConsentInformation) {
        if (!consentInformation.canRequestAds() || adsInitialized) return
        adsInitialized = true
        Thread {
            MobileAds.initialize(applicationContext) {
                runOnUiThread { adsMayLoad.value = true }
            }
        }.start()
    }

    private fun authenticateBiometric(onSuccess: () -> Unit) {
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        if (BiometricManager.from(this).canAuthenticate(authenticators) != BiometricManager.BIOMETRIC_SUCCESS) return

        val prompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }
            }
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock FinTrack")
            .setSubtitle("Authenticate to view your financial data")
            .setAllowedAuthenticators(authenticators)
            .build()
        prompt.authenticate(promptInfo)
    }
}

@androidx.compose.runtime.Composable
private fun AppLockScreen(
    showPin: Boolean,
    showBiometric: Boolean,
    pin: String,
    pinError: Boolean,
    onPinChange: (String) -> Unit,
    onSubmitPin: () -> Unit,
    onBiometric: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("FinTrack is locked", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))
        if (showPin) {
            OutlinedTextField(
                value = pin,
                onValueChange = onPinChange,
                label = { Text("PIN") },
                singleLine = true,
                isError = pinError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
            if (pinError) {
                Text("Incorrect PIN", color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onSubmitPin, enabled = pin.length in 4..8) {
                Text("Unlock")
            }
        }
        if (showBiometric) {
            Spacer(Modifier.height(8.dp))
            Button(onClick = onBiometric) {
                Icon(Icons.Default.Fingerprint, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Use biometrics or device lock")
            }
        }
    }
}

