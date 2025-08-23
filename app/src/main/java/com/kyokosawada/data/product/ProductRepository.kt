package com.kyokosawada.data.product

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Product, abstracts DB operations for MVVM.
 */
interface ProductRepository {
    fun getProducts(): Flow<List<ProductEntity>>
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    /**
     * Inserts a product and returns the new row ID.
     */
    suspend fun insertProduct(product: ProductEntity): Long
    suspend fun updateProduct(product: ProductEntity)
    suspend fun deleteProduct(product: ProductEntity)
    suspend fun getProductById(id: Long): ProductEntity?

    // Dashboard Analytics Methods

    /**
     * Get count of products with low stock.
     */
    suspend fun getLowStockCount(threshold: Int = 5): Int

    /**
     * Get products with low stock for alerts.
     */
    fun getLowStockProducts(threshold: Int = 5): Flow<List<ProductEntity>>

    /**
     * Get count of products with low stock as Flow for real-time updates.
     */
    fun getLowStockCountFlow(threshold: Int = 5): Flow<Int>
}
