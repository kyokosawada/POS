package com.kyokosawada.data.transaction

import kotlinx.coroutines.flow.Flow

/**
 * Implementation of TransactionRepository using Room DAO.
 */
class TransactionRepositoryImpl(private val dao: TransactionDao) : TransactionRepository {

    override suspend fun saveTransactionWithItems(
        transaction: TransactionEntity,
        items: List<CartItemEntity>
    ) {
        val transactionId = dao.insertTransaction(transaction)
        val updatedItems = items.map { it.copy(transactionId = transactionId) }
        dao.insertCartItems(updatedItems)
    }

    override fun getAllTransactionsWithItems(): Flow<List<TransactionWithItems>> =
        dao.getAllTransactionsWithItems()

    override fun getTransactionWithItemsById(id: Long): Flow<TransactionWithItems?> =
        dao.getTransactionWithItemsById(id)

    // Dashboard Analytics Implementation

    override suspend fun getSalesSummary(startDate: Long, endDate: Long): SalesSummaryResult =
        dao.getSalesSummary(startDate, endDate)

    override fun getSalesSummaryFlow(startDate: Long, endDate: Long): Flow<SalesSummaryResult> =
        dao.getSalesSummaryFlow(startDate, endDate)

    override fun getSalesByPaymentType(
        startDate: Long,
        endDate: Long
    ): Flow<List<SalesByPaymentType>> =
        dao.getSalesByPaymentType(startDate, endDate)

    override fun getTopProducts(
        startDate: Long,
        endDate: Long,
        limit: Int
    ): Flow<List<TopProduct>> =
        dao.getTopProducts(startDate, endDate, limit)

    override fun getDailySales(startDate: Long, endDate: Long): Flow<List<DailySalesResult>> =
        dao.getDailySales(startDate, endDate)
}
