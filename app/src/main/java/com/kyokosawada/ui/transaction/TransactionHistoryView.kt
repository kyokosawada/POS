package com.kyokosawada.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.kyokosawada.data.transaction.TransactionWithItems
import androidx.navigation.NavController
import androidx.compose.foundation.clickable

/**
 * Transaction History screen.
 */
@Composable
fun TransactionHistoryView(
    navController: NavController,
    viewModel: TransactionHistoryViewModel = koinViewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (transactions.isEmpty()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("No transactions yet.", style = MaterialTheme.typography.headlineMedium)
            }
        } else {
            LazyColumn {
                items(transactions) { tx ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(
                                    com.kyokosawada.ui.navigation.NavDestination.ReceiptDetail.createRoute(
                                        tx.transaction.id
                                    )
                                )
                            }
                    ) {
                        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Date: ${
                                    java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                                        .format(java.util.Date(tx.transaction.date))
                                }",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Total: ₱${"%.2f".format(tx.transaction.total)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
