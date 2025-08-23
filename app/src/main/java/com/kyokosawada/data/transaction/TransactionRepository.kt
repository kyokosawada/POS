package com.kyokosawada.data.transaction

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for transactions: save and fetch historical sales.
 */
interface TransactionRepository {
    suspend fun saveTransactionWithItems(
        transaction: TransactionEntity,
        items: List<CartItemEntity>
    )

    fun getAllTransactionsWithItems(): Flow<List<TransactionWithItems>>
    fun getTransactionWithItemsById(id: Long): Flow<TransactionWithItems?>

    // Dashboard Analytics Methods

    /**
     * Get sales summary for a date range.
     */
    suspend fun getSalesSummary(startDate: Long, endDate: Long): SalesSummaryResult

    /**
     * Get sales summary as Flow for real-time updates.
     */
    fun getSalesSummaryFlow(startDate: Long, endDate: Long): Flow<SalesSummaryResult>

    /**
     * Get sales breakdown by payment type.
     */
    fun getSalesByPaymentType(startDate: Long, endDate: Long): Flow<List<SalesByPaymentType>>

    /**
     * Get top selling products by quantity.
     */
    fun getTopProducts(startDate: Long, endDate: Long, limit: Int = 10): Flow<List<TopProduct>>

    /**
     * Get daily sales data for trend charts.
     */
    fun getDailySales(startDate: Long, endDate: Long): Flow<List<DailySalesResult>>
}
