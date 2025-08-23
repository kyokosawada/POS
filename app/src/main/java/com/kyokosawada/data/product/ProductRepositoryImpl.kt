package com.kyokosawada.data.product

import kotlinx.coroutines.flow.Flow

/**
 * Implementation of ProductRepository using Room DAO.
 */

class ProductRepositoryImpl(
    private val dao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<ProductEntity>> = dao.getProducts()

    override fun searchProducts(query: String): Flow<List<ProductEntity>> =
        dao.searchProducts("%$query%")

    override suspend fun insertProduct(product: ProductEntity): Long = dao.insertProduct(product)

    override suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)

    override suspend fun deleteProduct(product: ProductEntity) = dao.deleteProduct(product)

    override suspend fun getProductById(id: Long): ProductEntity? = dao.getProductById(id)

    // Dashboard Analytics Implementation

    override suspend fun getLowStockCount(threshold: Int): Int = dao.getLowStockCount(threshold)

    override fun getLowStockProducts(threshold: Int): Flow<List<ProductEntity>> =
        dao.getLowStockProducts(threshold)

    override fun getLowStockCountFlow(threshold: Int): Flow<Int> =
        dao.getLowStockCountFlow(threshold)
}
