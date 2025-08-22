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
}
