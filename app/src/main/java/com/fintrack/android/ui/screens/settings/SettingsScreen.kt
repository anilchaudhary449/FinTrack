package com.fintrack.android.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.android.data.preferences.ThemeMode
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.IncomeGreen

@Composable
fun SettingsScreen(
    onNavigateToAccounts: () -> Unit,
    onNavigateToDebts: () -> Unit,
    onNavigateToNetWorth: () -> Unit,
    onNavigateToAudit: () -> Unit,
    onNavigateToInsights: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            FinTopBar(title = "Settings & Preferences")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notification Messages
            if (state.backupSuccessMessage != null) {
                item {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = IncomeGreen.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.backupSuccessMessage!!,
                            color = IncomeGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }

            // Quick Navigation Hub
            item {
                Text(text = "Modules & Management", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                FinCard {
                    SettingsNavigationItem(
                        icon = Icons.Default.AccountBalance,
                        title = "Financial Accounts",
                        subtitle = "Manage cash, bank accounts & digital wallets",
                        onClick = onNavigateToAccounts
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.TrendingUp,
                        title = "Net Worth Calculator",
                        subtitle = "Total assets minus total liabilities",
                        onClick = onNavigateToNetWorth
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.MoneyOff,
                        title = "Debts & Loans",
                        subtitle = "Track money borrowed and money lent",
                        onClick = onNavigateToDebts
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.History,
                        title = "Financial Audit Trail",
                        subtitle = "Inspect immutable history of all records",
                        onClick = onNavigateToAudit
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Lightbulb,
                        title = "Spending Insights",
                        subtitle = "AI-powered observations & spending patterns",
                        onClick = onNavigateToInsights
                    )
                }
            }

            // Currency & Calendar Settings
            item {
                Text(text = "Localization & Currency", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                FinCard {
                    Text("Default Currency", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("NPR", "USD", "INR", "EUR", "GBP").forEach { curr ->
                            FilterChip(
                                selected = state.preferences.defaultCurrency == curr,
                                onClick = { viewModel.onCurrencyChange(curr) },
                                label = { Text(curr) }
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Bikram Sambat (BS) Dates", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text("Display dates in Nepali Bikram Sambat calendar format", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        Switch(
                            checked = state.preferences.isBikramSambatEnabled,
                            onCheckedChange = { viewModel.onBikramSambatToggle(it) }
                        )
                    }
                }
            }

            // Theme Preferences
            item {
                Text(text = "Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                FinCard {
                    Text("Theme Mode", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeMode.values().forEach { mode ->
                            FilterChip(
                                selected = state.preferences.themeMode == mode,
                                onClick = { viewModel.onThemeChange(mode) },
                                label = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                }
            }

            // Security & Privacy
            item {
                Text(text = "Security & Privacy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                FinCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Biometric / PIN Unlock", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text("Protect financial records with biometric or fingerprint authentication", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        Switch(
                            checked = state.preferences.isBiometricEnabled,
                            onCheckedChange = { viewModel.onBiometricToggle(it) }
                        )
                    }
                }
            }

            // Data Backup
            item {
                Text(text = "Data Backup & Recovery", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                FinCard {
                    Text(
                        text = "Full Encrypted Database Backup",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Create a verified local JSON backup of all accounts, transactions, and categories with cryptographic checksum.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.createBackup(context) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.Backup, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Local Backup Now")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsNavigationItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
    }
}

