package com.fintrack.android.ui.screens.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.fintrack.android.domain.model.Transaction
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.domain.repository.TransactionRepository
import com.fintrack.android.domain.util.CurrencyFormatter
import com.fintrack.android.ui.components.FinAmountDisplay
import com.fintrack.android.ui.components.FinCard
import com.fintrack.android.ui.components.FinConfirmationDialog
import com.fintrack.android.ui.components.FinTopBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Long = savedStateHandle.get<Long>("id") ?: -1L

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction: StateFlow<Transaction?> = _transaction

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    init {
        loadTransaction()
    }

    private fun loadTransaction() {
        if (transactionId > 0) {
            viewModelScope.launch {
                _transaction.value = transactionRepository.getTransactionById(transactionId)
            }
        }
    }

    fun deleteTransaction() {
        if (transactionId > 0) {
            viewModelScope.launch {
                transactionRepository.deleteTransaction(transactionId)
                _isDeleted.value = true
            }
        }
    }
}

@Composable
fun TransactionDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    val tx by viewModel.transaction.collectAsState()
    val isDeleted by viewModel.isDeleted.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            FinTopBar(
                title = "Transaction Details",
                onNavigateBack = onNavigateBack,
                actions = {
                    if (tx != null) {
                        IconButton(onClick = { onNavigateToEdit(tx!!.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (tx == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val item = tx!!
            val dateStr = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, hh:mm a")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(item.transactionDate))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Amount Card
                FinCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.type.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FinAmountDisplay(
                            amount = item.amount,
                            currencyCode = item.currency,
                            type = item.type,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }

                // Attributes List Card
                FinCard {
                    DetailRow(label = "Date & Time", value = dateStr)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    if (item.type != TransactionType.TRANSFER) {
                        DetailRow(label = "Category", value = item.categoryName.ifEmpty { "None" })
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    }
                    DetailRow(label = "Account", value = item.accountName)
                    if (item.type == TransactionType.TRANSFER && item.toAccountName.isNotEmpty()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                        DetailRow(label = "To Account", value = item.toAccountName)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    DetailRow(label = "Payment Method", value = item.paymentMethod.displayName)
                    if (item.notes.isNotBlank()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                        DetailRow(label = "Notes", value = item.notes)
                    }
                }

                // Receipt photo if attached
                if (item.receiptUri != null) {
                    FinCard {
                        Text(
                            text = "Receipt Photo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = item.receiptUri,
                            contentDescription = "Receipt Attachment",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        FinConfirmationDialog(
            title = "Delete Transaction",
            message = "Are you sure you want to delete this transaction? This will reverse the balance in your account.",
            onConfirm = { viewModel.deleteTransaction() },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

