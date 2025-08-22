package com.kyokosawada.data.transaction

/**
 * Implementation of TransactionRepository using Room TransactionDao.
 */
class TransactionRepositoryImpl(private val dao: TransactionDao) : TransactionRepository {
    override suspend fun saveTransactionWithItems(
        transaction: TransactionEntity,
        items: List<CartItemEntity>
    ) {
        val transactionId = dao.insertTransaction(transaction)
        // Set transactionId for all CartItemEntities
        val itemsWithTxId = items.map { it.copy(transactionId = transactionId) }
        dao.insertCartItems(itemsWithTxId)
    }

    override fun getAllTransactionsWithItems() = dao.getAllTransactionsWithItems()
    override fun getTransactionWithItemsById(id: Long) = dao.getTransactionWithItemsById(id)
}
