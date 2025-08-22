package com.kyokosawada.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.kyokosawada.ui.utils.WindowSizeClass
import com.kyokosawada.ui.utils.getAdaptiveValues
import org.koin.androidx.compose.koinViewModel
import com.kyokosawada.data.cart.CartItem
import com.kyokosawada.ui.product.ProductViewModel
import com.kyokosawada.data.product.ProductEntity
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Material3 Cart screen - fully Compose. Observes ViewModel's StateFlow for cart display+edit.
 * Directly defined as CartView for navigation.
 */
@Composable
fun CartView(
    viewModel: CartViewModel = koinViewModel(),
    productViewModel: ProductViewModel = koinViewModel(),
    windowSizeClass: WindowSizeClass
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val products by productViewModel.products.collectAsState()
    val total = cartItems.sumOf  { it.subtotal }
    val showDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val adaptiveValues = windowSizeClass.getAdaptiveValues()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Cart")
                    }
                },
                windowInsets = WindowInsets(0)
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(adaptiveValues.horizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total: ₱${"%.2f".format(total)}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(adaptiveValues.horizontalPadding / 2))
                Button(
                    onClick = { viewModel.checkout() },
                    enabled = cartItems.isNotEmpty()
                ) { Text("Checkout") }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Cart is empty.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onRemove = { viewModel.removeItem(item.productId) },
                            onQuantityChange = {
                                viewModel.updateItemQuantity(item.productId, it)
                            },
                            windowSizeClass = windowSizeClass
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = adaptiveValues.horizontalPadding,
                        bottom = adaptiveValues.horizontalPadding / 2
                    ),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedButton(
                    onClick = { viewModel.clearAll() },
                    enabled = cartItems.isNotEmpty(),
                ) { Text("Clear") }
            }
        }
        if (showDialog.value) {
            AddProductDialog(
                products = products,
                onAdd = { product ->
                    if (product.stockQty <= 0) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Cannot add: Product is out of stock!")
                        }
                    } else {
                        viewModel.addItem(
                            com.kyokosawada.data.cart.CartItem(
                                productId = product.id,
                                name = product.name,
                                price = product.price,
                                quantity = 1,
                                subtotal = product.price
                            )
                        )
                    }
                    showDialog.value = false
                },
                onDismiss = { showDialog.value = false },
                windowSizeClass = windowSizeClass
            )
        }
    }
}

@Composable
private fun AddProductDialog(
    products: List<ProductEntity>,
    onAdd: (ProductEntity) -> Unit,
    onDismiss: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val adaptiveValues = windowSizeClass.getAdaptiveValues()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Product to Cart") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (products.isEmpty()) {
                    Text("No products available")
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = (320.dp + adaptiveValues.horizontalPadding))) {
                        items(products) { product ->
                            val isOutOfStock = product.stockQty <= 0
                            ListItem(
                                headlineContent = { Text(product.name) },
                                supportingContent = { Text("₱${"%.2f".format(product.price)} | Stock: ${product.stockQty}") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .let { if (isOutOfStock) it else it.clickable { onAdd(product) } },
                                colors = ListItemDefaults.colors(
                                    containerColor = if (isOutOfStock) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                                ),
                                trailingContent = {
                                    if (isOutOfStock) Text(
                                        "Out of stock",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val adaptiveValues = windowSizeClass.getAdaptiveValues()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                vertical = adaptiveValues.horizontalPadding / 2,
                horizontal = adaptiveValues.horizontalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Text(
                "₱${"%.2f".format(item.price)} x ${item.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = "Remove") }
        OutlinedButton(
            onClick = { onQuantityChange(item.quantity - 1) }, enabled = item.quantity > 1
        ) { Text("-") }
        Text(
            "${item.quantity}",
            Modifier.padding(horizontal = adaptiveValues.horizontalPadding / 2)
        )
        OutlinedButton(onClick = { onQuantityChange(item.quantity + 1) }) { Text("+") }
    }
}