package com.kyokosawada.data.transaction

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Transaction and related CartItems; supports history and receipt logic.
 */
@Dao
interface TransactionDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItems(items: List<CartItemEntity>)

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsWithItems(): Flow<List<TransactionWithItems>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionWithItemsById(id: Long): Flow<TransactionWithItems?>
    // Dashboard Analytics Queries

    /**
     * Get total sales and transaction count for a date range.
     */
    @Query(
        """
        SELECT 
            COALESCE(SUM(total), 0.0) as totalSales,
            COUNT(*) as totalTransactions,
            COALESCE(AVG(total), 0.0) as averageTransactionValue
        FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
    """
    )
    suspend fun getSalesSummary(startDate: Long, endDate: Long): SalesSummaryResult

    /**
     * Get sales breakdown by payment type for a date range.
     */
    @Query(
        """
        SELECT 
            paymentType,
            COALESCE(SUM(total), 0.0) as total,
            COUNT(*) as count
        FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY paymentType
        ORDER BY total DESC
    """
    )
    fun getSalesByPaymentType(startDate: Long, endDate: Long): Flow<List<SalesByPaymentType>>

    /**
     * Get top selling products by quantity for a date range.
     */
    @Query(
        """
        SELECT 
            ci.productId,
            ci.name,
            SUM(ci.quantity) as quantitySold,
            SUM(ci.subtotal) as revenue
        FROM cart_items ci
        INNER JOIN transactions t ON ci.transactionId = t.id
        WHERE t.date BETWEEN :startDate AND :endDate
        GROUP BY ci.productId, ci.name
        ORDER BY quantitySold DESC
        LIMIT :limit
    """
    )
    fun getTopProducts(startDate: Long, endDate: Long, limit: Int = 10): Flow<List<TopProduct>>

    /**
     * Get daily sales data for trend charts.
     */
    @Query(
        """
        SELECT 
            date(date / 1000, 'unixepoch') as dateString,
            date,
            SUM(total) as sales,
            COUNT(*) as transactionCount
        FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY date(date / 1000, 'unixepoch')
        ORDER BY date ASC
    """
    )
    fun getDailySales(startDate: Long, endDate: Long): Flow<List<DailySalesResult>>

    /**
     * Get sales summary as a Flow for real-time updates.
     */
    @Query(
        """
        SELECT 
            COALESCE(SUM(total), 0.0) as totalSales,
            COUNT(*) as totalTransactions,
            COALESCE(AVG(total), 0.0) as averageTransactionValue
        FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
    """
    )
    fun getSalesSummaryFlow(startDate: Long, endDate: Long): Flow<SalesSummaryResult>
}

/**
 * Data class for sales summary query result.
 */
data class SalesSummaryResult(
    val totalSales: Double,
    val totalTransactions: Int,
    val averageTransactionValue: Double
)

/**
 * Data class for sales by payment type.
 */
data class SalesByPaymentType(
    val paymentType: String,
    val total: Double,
    val count: Int
)

/**
 * Data class for top products query result.
 */
data class TopProduct(
    val productId: Long,
    val name: String,
    val quantitySold: Int,
    val revenue: Double
)

/**
 * Data class for daily sales query result.
 */
data class DailySalesResult(
    val dateString: String,
    val date: Long,
    val sales: Double,
    val transactionCount: Int
)
