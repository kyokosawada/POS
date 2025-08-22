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
}
