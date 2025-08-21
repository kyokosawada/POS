package com.kyokosawada.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kyokosawada.data.ProductEntity

/**
 * Product Add/Edit View as reusable composable,
 * showing Material3 form, minimal validation,
 * with Save/Cancel callbacks gathering values.
 */
@Composable
fun ProductEditView(
    initial: ProductEntity? = null,
    onSave: (ProductEntity) -> Unit,
    onCancel: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf(initial?.name ?: "") }
    var price by rememberSaveable { mutableStateOf(if (initial == null) "" else initial.price.toString()) }
    var sku by rememberSaveable { mutableStateOf(initial?.sku ?: "") }
    var barcode by rememberSaveable { mutableStateOf(initial?.barcode ?: "") }
    var stockQty by rememberSaveable { mutableStateOf((initial?.stockQty ?: 0).toString()) }
    var category by rememberSaveable { mutableStateOf(initial?.category ?: "") }
    var imageUrl by rememberSaveable { mutableStateOf(initial?.imageUrl ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (initial == null) "Add Product" else "Edit Product",
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name*") },
                singleLine = true,
                isError = error != null && name.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Price*") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = error != null && price.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Barcode*") },
                singleLine = true,
                isError = error != null && barcode.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = stockQty,
                onValueChange = { stockQty = it.filter { ch -> ch.isDigit() } },
                label = { Text("Stock Quantity*") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = error != null && stockQty.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            // Optional field for imageUrl; advanced UI would hook this up to image picker
            OutlinedTextField(
                value = imageUrl ?: "",
                onValueChange = { imageUrl = it },
                label = { Text("Image URL") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) { Text("Cancel") }
                Button(
                    enabled = name.isNotBlank() && price.isNotBlank() && barcode.isNotBlank() && stockQty.isNotBlank(),
                    onClick = {
                        val parsedPrice = price.toDoubleOrNull()
                        val parsedQty = stockQty.toIntOrNull()
                        if (parsedPrice == null || parsedQty == null) {
                            error = "Invalid price or stock quantity"
                        } else {
                            error = null
                            onSave(
                                ProductEntity(
                                    id = initial?.id ?: 0L,
                                    name = name.trim(),
                                    price = parsedPrice,
                                    sku = sku.trim(),
                                    barcode = barcode.trim(),
                                    stockQty = parsedQty,
                                    category = category.trim(),
                                    imageUrl = imageUrl.takeIf { it.isNotBlank() },
                                    created = initial?.created ?: System.currentTimeMillis(),
                                    updated = System.currentTimeMillis()
                                )
                            )
                        }
                    }) {
                    Text(text = if (initial == null) "Save" else "Update")
                }
            }
        }
    }
}