package com.kyokosawada.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyokosawada.data.product.ProductRepository
import com.kyokosawada.data.transaction.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for Dashboard/Analytics screen.
 * Provides real-time analytics data for the dashboard UI.
 */
class DashboardViewModel(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _selectedDateRange = MutableStateFlow(DateRange.TODAY)
    val selectedDateRange = _selectedDateRange.asStateFlow()

    /**
     * Dashboard summary combining all data sources.
     */
    val dashboardSummary: StateFlow<DashboardSummary> = combine(
        selectedDateRange,
        productRepository.getLowStockCountFlow()
    ) { dateRange, lowStockCount ->
        val startDate = dateRange.getStartDate()
        val endDate = dateRange.getEndDate()

        try {
            val salesSummary = transactionRepository.getSalesSummary(startDate, endDate)
            DashboardSummary(
                totalSales = salesSummary.totalSales,
                totalTransactions = salesSummary.totalTransactions,
                averageTransactionValue = salesSummary.averageTransactionValue,
                lowStockCount = lowStockCount
            )
        } catch (e: Exception) {
            DashboardSummary() // Return default values on error
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardSummary()
    )

    /**
     * Sales breakdown by payment type.
     */
    val salesByPaymentType: StateFlow<List<SalesByPaymentType>> = selectedDateRange
        .flatMapLatest { dateRange ->
            transactionRepository.getSalesByPaymentType(
                dateRange.getStartDate(),
                dateRange.getEndDate()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Top selling products.
     */
    val topProducts: StateFlow<List<TopProduct>> = selectedDateRange
        .flatMapLatest { dateRange ->
            transactionRepository.getTopProducts(
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                limit = 5
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Daily sales data for trend chart.
     */
    val dailySales: StateFlow<List<DailySalesResult>> = selectedDateRange
        .flatMapLatest { dateRange ->
            transactionRepository.getDailySales(
                dateRange.getStartDate(),
                dateRange.getEndDate()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Low stock products for alerts.
     */
    val lowStockProducts: StateFlow<List<com.kyokosawada.data.product.ProductEntity>> =
        productRepository.getLowStockProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    /**
     * Update the selected date range for filtering.
     */
    fun selectDateRange(dateRange: DateRange) {
        _selectedDateRange.value = dateRange
    }


    /**
     * Format currency values for display.
     */
    fun formatCurrency(amount: Double): String {
        return "₱%.2f".format(amount)
    }

    /**
     * Format date for display.
     */
    fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    /**
     * Chart Y-series data (sales amounts) for Vico chart. Pre-processed to fit MVVM best practice.
     */
    val salesChartSeries: StateFlow<List<Double>> = dailySales.map { salesList ->
        salesList.map { it.sales }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Chart X-labels (dates) for Vico chart. Pre-processed to fit MVVM best practice.
     */
    val salesChartLabels: StateFlow<List<String>> = dailySales.map { salesList ->
        salesList.map {
            formatDate(it.date)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Get formatted labels for top products chart.
     */
    fun getTopProductsLabels(): List<String> {
        return topProducts.value.map {
            if (it.name.length > 10) "${it.name.take(10)}..." else it.name
        }
    }
}