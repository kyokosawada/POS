package com.kyokosawada.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyokosawada.data.product.ProductEntity
import com.kyokosawada.data.product.ProductDao
import org.koin.androidx.compose.koinViewModel

import com.kyokosawada.data.product.ProductViewModel
/**
 * Product Inventory List screen. MVVM: State from ProductViewModel via Koin.
 */
import androidx.compose.runtime.saveable.rememberSaveable
import com.kyokosawada.ui.product.ProductEditView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListView(
    viewModel: ProductViewModel = koinViewModel(),
    onAddProduct: () -> Unit = {},
    onProductClick: (ProductEntity) -> Unit = {}
) {
    val products = viewModel.products.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val showAddSheetState = rememberSaveable { mutableStateOf(false) }
    val showAddSheet = showAddSheetState.value
    val bottomSheetState = rememberModalBottomSheetState()
    val editProductState = rememberSaveable { mutableStateOf<ProductEntity?>(null) }
    val showEditSheet = editProductState.value != null

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val productToDeleteState = rememberSaveable { mutableStateOf<ProductEntity?>(null) }
    val productToDelete = productToDeleteState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Products")
                    }
                },
                windowInsets = WindowInsets(0)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheetState.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                error != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                products.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No products found.", style = MaterialTheme.typography.bodyLarge)
                }

                else -> Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(products, key = { it.id }) { product ->
                            ProductListItem(
                                product = product,
                                onClick = { editProductState.value = product },
                                onDelete = { productToDeleteState.value = product }
                            )
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }

    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDeleteState.value = null },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteProduct(productToDelete!!)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Product deleted")
                    }
                    productToDeleteState.value = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDeleteState.value = null }) { Text("Cancel") }
            },
            title = { Text("Delete Product") },
            text = {
                Text(
                    "Are you sure you want to delete \"${productToDelete?.name ?: ""}\"?"
                )
            }
        )
    }

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheetState.value = false },
            sheetState = bottomSheetState
        ) {
            ProductEditView(
                onSave = {
                    viewModel.addProduct(it)
                    showAddSheetState.value = false
                },
                onCancel = { showAddSheetState.value = false }
            )
        }
    }
    if (showEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { editProductState.value = null },
            sheetState = bottomSheetState
        ) {
            ProductEditView(
                initial = editProductState.value,
                onSave = {
                    viewModel.updateProduct(it)
                    editProductState.value = null
                },
                onCancel = { editProductState.value = null }
            )
        }
    }
}

@Composable
fun ProductListItem(
    product: ProductEntity,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val menuExpandedState = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "₱" + String.format("%.2f", product.price),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "Stock: ${product.stockQty}", style = MaterialTheme.typography.bodySmall)
            if (!product.sku.isNullOrBlank()) Text("SKU: ${product.sku}", style = MaterialTheme.typography.labelSmall)
        }
        Box {
            IconButton(onClick = { menuExpandedState.value = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options")
            }
            DropdownMenu(
                expanded = menuExpandedState.value,
                onDismissRequest = { menuExpandedState.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        menuExpandedState.value = false
                        onDelete()
                    },
                    leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Delete") }
                )
            }
        }
    }
}
