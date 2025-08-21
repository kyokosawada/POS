package com.kyokosawada.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository for Product, abstracts DB operations for MVVM.
 */
class ProductRepository(private val dao: ProductDao) {
    fun getProducts(): Flow<List<ProductEntity>> = dao.getProducts()
    fun searchProducts(query: String): Flow<List<ProductEntity>> = dao.searchProducts("%$query%")
    suspend fun insertProduct(product: ProductEntity) = dao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)
    suspend fun deleteProduct(product: ProductEntity) = dao.deleteProduct(product)
    suspend fun getProductById(id: Long) = dao.getProductById(id)
}
