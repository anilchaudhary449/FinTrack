package com.fintrack.android.ui.screens.transaction

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fintrack.android.domain.model.CategoryType
import com.fintrack.android.domain.model.PaymentMethod
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinTopBar
import com.fintrack.android.ui.theme.ExpenseRed
import com.fintrack.android.ui.theme.IncomeGreen
import com.fintrack.android.ui.theme.TransferBlue
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditTransactionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Photo picker launcher for receipts
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onReceiptUriChange(uri?.toString())
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            FinTopBar(
                title = if (state.id != null) "Edit Transaction" else "Add Transaction",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Selector Tabs (Expense / Income / Transfer)
            TabRow(
                selectedTabIndex = when (state.type) {
                    TransactionType.EXPENSE -> 0
                    TransactionType.INCOME -> 1
                    TransactionType.TRANSFER -> 2
                    else -> 0
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Tab(
                    selected = state.type == TransactionType.EXPENSE,
                    onClick = { viewModel.onTypeChange(TransactionType.EXPENSE) },
                    text = {
                        Text(
                            text = "Expense",
                            fontWeight = FontWeight.Bold,
                            color = if (state.type == TransactionType.EXPENSE) ExpenseRed else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                Tab(
                    selected = state.type == TransactionType.INCOME,
                    onClick = { viewModel.onTypeChange(TransactionType.INCOME) },
                    text = {
                        Text(
                            text = "Income",
                            fontWeight = FontWeight.Bold,
                            color = if (state.type == TransactionType.INCOME) IncomeGreen else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                Tab(
                    selected = state.type == TransactionType.TRANSFER,
                    onClick = { viewModel.onTypeChange(TransactionType.TRANSFER) },
                    text = {
                        Text(
                            text = "Transfer",
                            fontWeight = FontWeight.Bold,
                            color = if (state.type == TransactionType.TRANSFER) TransferBlue else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }

            // Amount Input Card
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter Amount",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "रु",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = state.amountString,
                            onValueChange = { viewModel.onAmountChange(it) },
                            textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            placeholder = { Text("0.00") },
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }

            // Error Message
            if (state.errorMessage != null) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Category Selector (If not Transfer)
            if (state.type != TransactionType.TRANSFER) {
                val targetCatType = if (state.type == TransactionType.INCOME) CategoryType.INCOME else CategoryType.EXPENSE
                val availableCats = state.categories.filter { it.type == targetCatType }

                Column {
                    Text(text = "Category", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val catScrollState = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(catScrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableCats.forEach { cat ->
                            FilterChip(
                                selected = state.selectedCategoryId == cat.id,
                                onClick = { viewModel.onCategoryChange(cat.id) },
                                label = { Text(cat.name) }
                            )
                        }
                    }
                }
            }

            // Account Selector
            Column {
                val label = if (state.type == TransactionType.TRANSFER) "From Account" else "Account"
                Text(text = label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                val accScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(accScrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.accounts.forEach { acc ->
                        FilterChip(
                            selected = state.selectedAccountId == acc.id,
                            onClick = { viewModel.onAccountChange(acc.id) },
                            label = { Text("${acc.name} (${CurrencyFormatter.format(acc.currentBalance)})") }
                        )
                    }
                }
            }

            // Destination Account Selector (Transfer only)
            if (state.type == TransactionType.TRANSFER) {
                Column {
                    Text(text = "To Account", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val toAccScrollState = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(toAccScrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.accounts.filter { it.id != state.selectedAccountId }.forEach { acc ->
                            FilterChip(
                                selected = state.selectedToAccountId == acc.id,
                                onClick = { viewModel.onToAccountChange(acc.id) },
                                label = { Text("${acc.name} (${CurrencyFormatter.format(acc.currentBalance)})") }
                            )
                        }
                    }
                }
            }

            // Payment Method Selector
            Column {
                Text(text = "Payment Method", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                val pmScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(pmScrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentMethod.values().forEach { method ->
                        FilterChip(
                            selected = state.selectedPaymentMethod == method,
                            onClick = { viewModel.onPaymentMethodChange(method) },
                            label = { Text(method.displayName) }
                        )
                    }
                }
            }

            // Notes TextField
            OutlinedTextField(
                value = state.notes,
                onValueChange = { viewModel.onNotesChange(it) },
                label = { Text("Notes & Description (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            // Receipt Photo Attachment
            Column {
                Text(text = "Receipt Attachment", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                if (state.receiptUri != null) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = state.receiptUri,
                            contentDescription = "Receipt Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { viewModel.onReceiptUriChange(null) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remove receipt", tint = ExpenseRed)
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Attach Receipt Photo")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveTransaction() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Save Transaction", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

