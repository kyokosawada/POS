package com.kyokosawada.data.transaction

/**
 * Data models for dashboard analytics and reporting.
 */

/**
 * Summary data for dashboard overview cards.
 */
data class DashboardSummary(
    val totalSales: Double = 0.0,
    val totalTransactions: Int = 0,
    val averageTransactionValue: Double = 0.0,
    val lowStockCount: Int = 0
)


/**
 * Daily sales data for trend charts.
 */
data class DailySales(
    val date: Long,
    val sales: Double,
    val transactionCount: Int
)

/**
 * Date range options for dashboard filtering.
 */
enum class DateRange(val label: String) {
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month"),
    THIS_YEAR("This Year")
}

/**
 * Extension functions to get date range boundaries.
 */
fun DateRange.getStartDate(): Long {
    val now = System.currentTimeMillis()
    val calendar = java.util.Calendar.getInstance()

    return when (this) {
        DateRange.TODAY -> {
            calendar.timeInMillis = now
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }

        DateRange.THIS_WEEK -> {
            calendar.timeInMillis = now
            calendar.set(java.util.Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }

        DateRange.THIS_MONTH -> {
            calendar.timeInMillis = now
            calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }

        DateRange.THIS_YEAR -> {
            calendar.timeInMillis = now
            calendar.set(java.util.Calendar.DAY_OF_YEAR, 1)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }
    }
}

fun DateRange.getEndDate(): Long = System.currentTimeMillis()