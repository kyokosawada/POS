package com.kyokosawada.data.product

import androidx.room.*
import com.kyokosawada.data.product.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Product, Room persistence.
 * Provides CRUD and search, Rx via Flow for Compose.
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE name LIKE :query OR barcode LIKE :query ORDER BY name ASC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    // Dashboard Analytics Queries

    /**
     * Get count of products with low stock (below threshold).
     */
    @Query("SELECT COUNT(*) FROM products WHERE stockQty <= :threshold")
    suspend fun getLowStockCount(threshold: Int = 5): Int

    /**
     * Get products with low stock for alerts.
     */
    @Query("SELECT * FROM products WHERE stockQty <= :threshold ORDER BY stockQty ASC")
    fun getLowStockProducts(threshold: Int = 5): Flow<List<ProductEntity>>

    /**
     * Get count of products with low stock as Flow for real-time updates.
     */
    @Query("SELECT COUNT(*) FROM products WHERE stockQty <= :threshold")
    fun getLowStockCountFlow(threshold: Int = 5): Flow<Int>
}
