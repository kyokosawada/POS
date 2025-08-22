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
}
