package com.kyokosawada.ui.product

import android.annotation.SuppressLint
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

import com.kyokosawada.ui.product.ProductViewModel
/**
 * Product Inventory List screen. MVVM: State from ProductViewModel via Koin.
 */

// --- Window size adaptivity ---
import com.kyokosawada.ui.utils.calculateWindowSizeClass
import com.kyokosawada.ui.utils.getAdaptiveValues
import com.kyokosawada.ui.utils.WindowWidthSizeClass
import com.kyokosawada.ui.utils.AdaptiveValues
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListView(
    viewModel: ProductViewModel = koinViewModel(),
    onProductClick: (ProductEntity) -> Unit = {}
) {
    val windowSizeClass = calculateWindowSizeClass()
    val adaptiveValues = windowSizeClass.getAdaptiveValues()

    val products = viewModel.products.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val showAddSheetState = rememberSaveable { mutableStateOf(false) }
    val showAddSheet = showAddSheetState.value
    val editProductState = rememberSaveable { mutableStateOf<ProductEntity?>(null) }
    val showEditSheet = editProductState.value != null
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val productToDeleteState = rememberSaveable { mutableStateOf<ProductEntity?>(null) }
    val productToDelete = productToDeleteState.value

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheetState.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Product")
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                ProductHeader()
                ProductContent(
                    products = products,
                    loading = loading,
                    error = error,
                    windowSizeClass = windowSizeClass.widthSizeClass,
                    adaptiveValues = adaptiveValues,
                    onEdit = { editProductState.value = it },
                    onDelete = { productToDeleteState.value = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
            ProductDeleteDialog(
                productToDelete = productToDelete,
                onDismiss = { productToDeleteState.value = null },
                onDelete = {
                    viewModel.deleteProduct(productToDelete!!)
                    coroutineScope.launch { snackbarHostState.showSnackbar("Product deleted") }
                    productToDeleteState.value = null
                }
            )
            ProductAddEditSheet(
                showSheet = showAddSheet,
                onDismiss = { showAddSheetState.value = false },
                onSave = {
                    viewModel.addProduct(it)
                    showAddSheetState.value = false
                }
            )
            ProductAddEditSheet(
                showSheet = showEditSheet,
                onDismiss = { editProductState.value = null },
                initial = editProductState.value,
                onSave = {
                    viewModel.updateProduct(it)
                    editProductState.value = null
                }
            )
        }
    }
}

@Composable
private fun ProductHeader() {
    Text(
        text = "Products",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun ProductContent(
    products: List<ProductEntity>,
    loading: Boolean,
    error: String?,
    windowSizeClass: WindowWidthSizeClass,
    adaptiveValues: AdaptiveValues,
    onEdit: (ProductEntity) -> Unit,
    onDelete: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        error != null -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text(text = error, color = MaterialTheme.colorScheme.error) }

        products.isEmpty() -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text(text = "No products found.", style = MaterialTheme.typography.bodyLarge) }

        else -> if (windowSizeClass == WindowWidthSizeClass.Expanded) {
            // Grid for expanded
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = adaptiveValues.cardMaxWidth),
                modifier = modifier
            ) {
                items(products, key = { it.id }) { product ->
                    ProductListItem(
                        product = product,
                        onClick = { onEdit(product) },
                        onDelete = { onDelete(product) },
                        cardMaxWidth = adaptiveValues.cardMaxWidth,
                        cardPadding = adaptiveValues.cardPadding
                    )
                    Divider(
                        modifier = Modifier.padding(
                            horizontal = adaptiveValues.horizontalPadding,
                            vertical = adaptiveValues.dividerSpacing
                        )
                    )
                }
            }
        } else {
            // List for compact
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(products, key = { it.id }) { product ->
                    ProductListItem(
                        product = product,
                        onClick = { onEdit(product) },
                        onDelete = { onDelete(product) },
                        cardMaxWidth = adaptiveValues.cardMaxWidth,
                        cardPadding = adaptiveValues.cardPadding
                    )
                    Divider(
                        modifier = Modifier.padding(
                            horizontal = adaptiveValues.horizontalPadding,
                            vertical = adaptiveValues.dividerSpacing
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDeleteDialog(
    productToDelete: ProductEntity?,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDelete) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
            title = { Text("Delete Product") },
            text = { Text("Are you sure you want to delete \"${productToDelete.name}\"?") }
        )
    }
}

@Composable
private fun ProductAddEditSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    initial: ProductEntity? = null,
    onSave: (ProductEntity) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState
        ) {
            ProductEditView(
                initial = initial,
                onSave = {
                    onSave(it)
                    onDismiss()
                },
                onCancel = onDismiss
            )
        }
    }
}

@Composable
fun ProductListItem(
    product: ProductEntity,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    cardMaxWidth: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp.Infinity,
    cardPadding: androidx.compose.ui.unit.Dp = 16.dp
) {
    val menuExpandedState = remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (cardMaxWidth != androidx.compose.ui.unit.Dp.Infinity) Modifier.widthIn(max = cardMaxWidth) else Modifier)
            .padding(horizontal = cardPadding, vertical = 8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "\u20b1" + String.format("%.2f", product.price),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Stock: ${product.stockQty}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!product.sku.isNullOrBlank()) Text(
                    text = "SKU: ${product.sku}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Box {
                IconButton(onClick = { menuExpandedState.value = true }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    }
}
