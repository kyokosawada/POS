package com.kyokosawada.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp // For getDashboardChartHeight return type
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import com.kyokosawada.data.transaction.*
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlin.math.roundToInt

import com.kyokosawada.ui.utils.WindowSizeClass
import com.kyokosawada.ui.utils.DeviceType
import com.kyokosawada.ui.utils.getAdaptiveValues
import com.kyokosawada.ui.utils.toDeviceType

/**
 * Dashboard/Home view with real-time analytics.
 */
/**
 * Responsive chart/card height based on window size for Material best practices.
 */
fun getDashboardChartHeight(windowSizeClass: WindowSizeClass): Dp =
    when (windowSizeClass.toDeviceType()) {
        DeviceType.MobilePortrait, DeviceType.MobileLandscape -> 220.dp
        DeviceType.TabletPortrait, DeviceType.TabletLandscape -> 320.dp
        DeviceType.Desktop -> 400.dp
    }

/**
 * Dashboard/Home view with real-time analytics.
 * Uses windowSizeClass for full responsive sizing of charts/cards.
 */
@Composable
fun DashboardView(
    windowSizeClass: WindowSizeClass,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val selectedDateRange by viewModel.selectedDateRange.collectAsStateWithLifecycle()
    val dashboardSummary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val salesByPaymentType by viewModel.salesByPaymentType.collectAsStateWithLifecycle()
    val topProducts by viewModel.topProducts.collectAsStateWithLifecycle()
    val salesChartSeries by viewModel.salesChartSeries.collectAsStateWithLifecycle()
    val salesChartLabels by viewModel.salesChartLabels.collectAsStateWithLifecycle()
    val lowStockProducts by viewModel.lowStockProducts.collectAsStateWithLifecycle()

    // Get device-responsive padding from utils
    val adaptive = windowSizeClass.getAdaptiveValues()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(adaptive.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(adaptive.cardContentSpacing)
    ) {
        // Header with date range selector
        item {
            DashboardHeader(
                selectedDateRange = selectedDateRange,
                onDateRangeSelected = viewModel::selectDateRange
            )
        }

        // Summary cards
        item {
            SummaryCards(
                summary = dashboardSummary,
                formatCurrency = viewModel::formatCurrency
            )
        }

        // Sales trend: only show for week, month, year.
        // Chart height is now responsive to device type.
        if (selectedDateRange != DateRange.TODAY) {
            item {
                SalesTrendLineChart(
                    salesSeries = salesChartSeries,
                    salesLabels = salesChartLabels,
                    chartHeight = getDashboardChartHeight(windowSizeClass)
                )
            }
        }

        // Top products list
        item {
            TopProductsList(
                topProducts = topProducts,
                formatCurrency = viewModel::formatCurrency
            )
        }

        // Payment type breakdown
        item {
            PaymentTypeBreakdown(
                salesByPaymentType = salesByPaymentType,
                formatCurrency = viewModel::formatCurrency
            )
        }

        // Low stock alerts
        if (lowStockProducts.isNotEmpty()) {
            item {
                LowStockAlerts(lowStockProducts = lowStockProducts)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardHeader(
    selectedDateRange: DateRange,
    onDateRangeSelected: (DateRange) -> Unit
) {
    Column {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(DateRange.values()) { range ->
                FilterChip(
                    onClick = { onDateRangeSelected(range) },
                    label = { Text(range.label) },
                    selected = selectedDateRange == range
                )
            }
        }
    }
}

@Composable
private fun SummaryCards(
    summary: DashboardSummary,
    formatCurrency: (Double) -> String
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = "Total Sales",
                value = formatCurrency(summary.totalSales),
                icon = Icons.Default.Home,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            SummaryCard(
                title = "Transactions",
                value = summary.totalTransactions.toString(),
                icon = Icons.Default.ShoppingCart,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        item {
            SummaryCard(
                title = "Avg. Sale",
                value = formatCurrency(summary.averageTransactionValue),
                icon = Icons.Default.List,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        if (summary.lowStockCount > 0) {
            item {
                SummaryCard(
                    title = "Low Stock",
                    value = summary.lowStockCount.toString(),
                    icon = Icons.Default.Warning,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Sales trend line chart visualizing daily POS sales data in real time.
 * Renders short date labels on X-axis and shows empty state if data absent.
 *
 * Defensive implementation for Vico (2024+): never reference AxisItemPlacer (not present),
 * never attempt to render chart for <2 points, and always provide a non-blank, non-null
 * string label for valueFormatter.
 *
 * @param modifier Modifier for chart card.
 * @param salesSeries List<Double>, must have at least 2 entries to show chart.
 * @param salesLabels List<String>, X-axis short labels, must match salesSeries.size.
 * @param chartHeight Dp, responsive chart height.
 */
@Composable
private fun SalesTrendLineChart(
    modifier: Modifier = Modifier,
    salesSeries: List<Double> = emptyList(),
    salesLabels: List<String> = emptyList(),
    chartHeight: Dp // Responsive chart height from DashboardView
) {
    // Defensive: Do NOT attempt to render chart or axes unless truly enough for a trend
    if (salesSeries.size < 2) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (salesSeries.isEmpty()) "No sales data available" else "Need at least 2 data points to show trend", 
                    style = MaterialTheme.typography.bodyMedium, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    // At least 2 points; proceed to render chart.
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(salesSeries) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    x = salesSeries.indices.toList(),
                    y = salesSeries
                )
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Sales Trend",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            // DO NOT attempt to specify itemPlacer; rely on default Vico axis tick/label stepping.
            // Defensive: Always provide a non-null, non-empty label string for every tick Vico asks.
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = { _, value, _ ->
                            val idx = value.toInt()
                            if (
                                idx >= 0 &&
                                idx < salesLabels.size &&
                                value.toInt().toDouble() == value
                            ) {
                                salesLabels[idx]
                            } else "?" // fallback; never empty/blank/null
                        }
                    ),
                ),
                modelProducer = modelProducer,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TopProductsList(
    topProducts: List<TopProduct>,
    formatCurrency: (Double) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Top Products",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (topProducts.isNotEmpty()) {
                topProducts.forEach { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${product.quantitySold} sold",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = formatCurrency(product.revenue),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (product != topProducts.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp
                        )
                    }
                }
            } else {
                Text(
                    text = "No product data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PaymentTypeBreakdown(
    salesByPaymentType: List<SalesByPaymentType>,
    formatCurrency: (Double) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Payment Methods",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (salesByPaymentType.isNotEmpty()) {
                salesByPaymentType.forEach { payment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = payment.paymentType,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = formatCurrency(payment.total),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${payment.count} transactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (payment != salesByPaymentType.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp
                        )
                    }
                }
            } else {
                Text(
                    text = "No payment data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun LowStockAlerts(
    lowStockProducts: List<com.kyokosawada.data.product.ProductEntity>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Low Stock Alert",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            lowStockProducts.take(5).forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "${product.stockQty} left",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (lowStockProducts.size > 5) {
                Text(
                    text = "and ${lowStockProducts.size - 5} more...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}