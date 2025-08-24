package com.kyokosawada.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import org.koin.androidx.compose.koinViewModel
import com.kyokosawada.data.transaction.TransactionWithItems
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Divider
import com.kyokosawada.ui.utils.calculateWindowSizeClass
import com.kyokosawada.ui.utils.getAdaptiveValues

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

    // Get window size class + adaptive values for responsive paddings, widths, spacing
    val windowSizeClass = calculateWindowSizeClass()
    val adaptiveValues = windowSizeClass.getAdaptiveValues()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Bold, padded header, now without icon
            Text(
                text = "Receipt",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = adaptiveValues.horizontalPadding,vertical = 8.dp)
            )
            // Show placeholder if no transaction is loaded
            if (selectedTransaction == null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "No receipt selected.",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            } else {
                val tx = selectedTransaction!!
                // Receipt summary card: adaptive max width, horizontal padding
                ElevatedCard(
                    modifier = Modifier
                        .padding(adaptiveValues.horizontalPadding)
                        .fillMaxWidth()
                        .widthIn(max = adaptiveValues.cardMaxWidth),
                    shape = RoundedCornerShape(20.dp),
                    elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp
                    ),
                    colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    // Main card content padding is adaptive per screen size
                    Column(modifier = Modifier.padding(adaptiveValues.cardPadding)) {
                        // Receipt high-level info
                        Text(
                            "Receipt #${tx.transaction.id}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Date: ${
                                java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    .format(java.util.Date(tx.transaction.date))
                            }",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        if (!tx.transaction.paymentType.isNullOrBlank()) {
                            Text(
                                "Payment: ${tx.transaction.paymentType}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(top = 3.dp)
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = adaptiveValues.dividerSpacing))
                        // Item list heading
                        Text(
                            "Items",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Item rows (adaptive vertical spacing)
                        tx.cartItems.forEachIndexed { idx, item ->
                            Row(modifier = Modifier.padding(vertical = 4.dp * adaptiveValues.spacingScale)) {
                                Text(
                                    "${item.quantity} x",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(36.dp)
                                )
                                Text(
                                    item.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "₱${"%.2f".format(item.price)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                                Text(
                                    "= ₱${"%.2f".format(item.subtotal)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                            // Divider between items except after last, adaptive vertical padding
                            if (idx != tx.cartItems.lastIndex) {
                                Divider(
                                    modifier = Modifier.padding(
                                        horizontal = 6.dp,
                                        vertical = adaptiveValues.dividerSpacing / 2
                                    ),
                                    thickness = 0.5.dp
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = adaptiveValues.dividerSpacing))
                        // Total at the bottom, green and bold
                        Text(
                            "Total: ₱${"%.2f".format(tx.transaction.total)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
