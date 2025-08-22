package com.kyokosawada.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import org.koin.androidx.compose.koinViewModel
import com.kyokosawada.data.transaction.TransactionWithItems
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

/**
 * Receipt PDF Preview.
 */
@Composable
fun ReceiptPreviewView(
    transactionId: Long,
    viewModel: TransactionHistoryViewModel = koinViewModel()
) {
    LaunchedEffect(transactionId) {
        if (transactionId > 0) {
            viewModel.loadTransactionDetail(transactionId)
        }
    }
    val selectedTransaction by viewModel.selectedTransaction.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (selectedTransaction == null) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("No receipt selected.", style = MaterialTheme.typography.headlineMedium)
            }
        } else {
            val tx = selectedTransaction!!
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text(
                        "Receipt #${tx.transaction.id}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Date: ${
                            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                                .format(java.util.Date(tx.transaction.date))
                        }"
                    )
                    Text("Payment: ${tx.transaction.paymentType}")
                }
                items(tx.cartItems) { item ->
                    Text(
                        "${item.name} x ${item.quantity} @ ₱${"%.2f".format(item.price)} = ₱${
                            "%.2f".format(
                                item.subtotal
                            )
                        }"
                    )
                }
                item {
                    Text(
                        "Total: ₱${"%.2f".format(tx.transaction.total)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
