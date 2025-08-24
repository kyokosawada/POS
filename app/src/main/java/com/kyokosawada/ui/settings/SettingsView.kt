package com.kyokosawada.ui.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.koin.androidx.compose.koinViewModel
import com.kyokosawada.ui.utils.calculateWindowSizeClass
import com.kyokosawada.ui.utils.getAdaptiveValues
import com.kyokosawada.ui.settings.SettingsViewModel
import androidx.compose.foundation.layout.WindowInsets

@Composable
private fun SettingsCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            content()
        }
    }
}

@Composable
private fun CurrencySettingRow(
    currency: String,
    onCurrencyChanged: (String) -> Unit,
    rowSpacing: Dp
) {
    val currencies = listOf("PHP", "USD", "EUR", "JPY")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = rowSpacing)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Default Currency",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = currency,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select Currency")
                    }
                },
                shape = MaterialTheme.shapes.medium
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onCurrencyChanged(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaxRateSettingRow(
    taxRate: Double,
    onTaxRateChanged: (Double) -> Unit,
    rowSpacing: Dp
) {
    var input by remember { mutableStateOf(if (taxRate > 0) taxRate.toString() else "") }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = rowSpacing)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tax Rate",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedTextField(
            value = input,
            onValueChange = { raw ->
                val filtered = buildString {
                    var decimalAdded = false
                    for (c in raw) {
                        if (c.isDigit()) append(c)
                        else if (c == '.' && !decimalAdded) {
                            append(c)
                            decimalAdded = true
                        }
                    }
                }
                input = filtered
                val number = filtered.toDoubleOrNull()
                if (filtered.isEmpty() || number != null) {
                    isError = false
                    if (number != null) onTaxRateChanged(number)
                } else {
                    isError = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Text(
                    "%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = isError,
            supportingText = {
                if (isError) Text(
                    "Please enter a valid number",
                    color = MaterialTheme.colorScheme.error
                )
            },
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
private fun SettingsHeader() {
    Text(
        text = "Settings",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * SettingsView: MVVM root composable for settings/branding/preferences screen.
 * Follows Material3, Compose best practices.
 */
@Composable
fun SettingsView(
    modifier: Modifier = Modifier
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val settings by viewModel.settings.collectAsState()
    val windowSizeClass = calculateWindowSizeClass()
    val adaptiveValues = windowSizeClass.getAdaptiveValues()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingsHeader()
        // Branding Section
        SettingsCard(
            title = "Business Information",
            icon = Icons.Filled.Home
        ) {
            ShopNameSettingRow(
                shopName = settings.shopName,
                onShopNameChanged = { viewModel.updateShopName(it) },
                fieldSpacing = 8.dp
            )
        }
        // Appearance Section
        SettingsCard(
            title = "Appearance",
            icon = Icons.Filled.Settings
        ) {
            DarkModeSettingRow(
                darkModeEnabled = settings.darkMode,
                onDarkModeToggled = { viewModel.updateDarkMode(it) },
                rowSpacing = 8.dp
            )
        }
        // Financial Configuration Section
        SettingsCard(
            title = "Financial Configuration",
            icon = Icons.Filled.Star
        ) {
            CurrencySettingRow(
                currency = settings.currency,
                onCurrencyChanged = { viewModel.updateCurrency(it) },
                rowSpacing = 8.dp
            )
            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            TaxRateSettingRow(
                taxRate = settings.taxRate,
                onTaxRateChanged = { viewModel.updateTaxRate(it) },
                rowSpacing = 8.dp
            )
        }
        // TODO: Additional settings sections for the future...
    }
}

@Composable
private fun ShopNameSettingRow(
    shopName: String,
    onShopNameChanged: (String) -> Unit,
    fieldSpacing: Dp
) {
    Column(modifier = Modifier.padding(vertical = fieldSpacing)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Business Name",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedTextField(
            value = shopName,
            onValueChange = onShopNameChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            placeholder = { Text("Enter your business name") }
        )
    }
}

@Composable
private fun DarkModeSettingRow(
    darkModeEnabled: Boolean,
    onDarkModeToggled: (Boolean) -> Unit,
    rowSpacing: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = rowSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (darkModeEnabled) Icons.Filled.Star else Icons.Filled.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (darkModeEnabled) "Dark theme enabled" else "Light theme enabled",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = darkModeEnabled,
            onCheckedChange = onDarkModeToggled
        )
    }
}


