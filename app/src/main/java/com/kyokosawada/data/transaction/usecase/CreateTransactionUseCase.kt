package com.kyokosawada.data.transaction.usecase

import com.kyokosawada.data.transaction.TransactionRepository
import com.kyokosawada.data.transaction.TransactionEntity
import com.kyokosawada.data.transaction.CartItemEntity

/**
 * Use case for saving a new transaction and its items (after checkout).
 */
class CreateTransactionUseCase(private val repo: TransactionRepository) {
    suspend operator fun invoke(transaction: TransactionEntity, items: List<CartItemEntity>) {
        repo.saveTransactionWithItems(transaction, items)
    }
}
