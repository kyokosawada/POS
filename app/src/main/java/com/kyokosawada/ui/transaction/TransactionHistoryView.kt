package com.kyokosawada.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import com.kyokosawada.data.transaction.TransactionWithItems
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.font.FontWeight

// Adaptivity utilities
import com.kyokosawada.ui.utils.calculateWindowSizeClass
import com.kyokosawada.ui.utils.getAdaptiveValues
import com.kyokosawada.ui.utils.WindowWidthSizeClass
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.widthIn

/**
 * Transaction History screen.
 */
@Composable
fun TransactionHistoryView(
    navController: NavController,
    viewModel: TransactionHistoryViewModel = koinViewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val windowSizeClass = calculateWindowSizeClass()
    val adaptiveValues = windowSizeClass.getAdaptiveValues()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Use a Column for top-aligned header and summary
        androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxSize()) {
            // Header for Receipts screen. MD3: headlineLarge, bold, padded
            Text(
                text = "Receipts",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = adaptiveValues.horizontalPadding,
                    vertical = 8.dp
                )
            )
            // Receipt summary: number of receipts if any present
            if (transactions.isNotEmpty()) {
                Text(
                    text = "${transactions.size} receipts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(
                        start = adaptiveValues.horizontalPadding,
                        bottom = 8.dp
                    )
                )
            }
            // Responsive list/grid layout
            if (transactions.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    // Empty state for receipts/history
                    Text("No transactions yet.", style = MaterialTheme.typography.headlineMedium)
                }
            } else if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                // Use grid for expanded screens
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = adaptiveValues.cardMaxWidth),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(transactions) { tx ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = adaptiveValues.cardMaxWidth)
                                .padding(horizontal = adaptiveValues.cardPadding, vertical = 8.dp)
                                .clickable {
                                    navController.navigate(
                                        com.kyokosawada.ui.navigation.NavDestination.ReceiptDetail.createRoute(
                                            tx.transaction.id
                                        )
                                    )
                                },
                            elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(
                                defaultElevation = 8.dp
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(adaptiveValues.cardPadding)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(end = 16.dp)
                                )
                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text(
                                        text = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                                            .format(java.util.Date(tx.transaction.date)),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Total: ₱${"%.2f".format(tx.transaction.total)}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                    if (!tx.transaction.paymentType.isNullOrBlank()) {
                                        Text(
                                            text = "Payment: ${tx.transaction.paymentType}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Compact/medium: single column
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(transactions) { tx ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = adaptiveValues.cardMaxWidth)
                                .padding(horizontal = adaptiveValues.cardPadding, vertical = 8.dp)
                                .clickable {
                                    navController.navigate(
                                        com.kyokosawada.ui.navigation.NavDestination.ReceiptDetail.createRoute(
                                            tx.transaction.id
                                        )
                                    )
                                },
                            elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(
                                defaultElevation = 8.dp
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(adaptiveValues.cardPadding)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(end = 16.dp)
                                )
                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text(
                                        text = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(tx.transaction.date)),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Total: ₱${"%.2f".format(tx.transaction.total)}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                    if (!tx.transaction.paymentType.isNullOrBlank()) {
                                        Text(
                                            text = "Payment: ${tx.transaction.paymentType}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
