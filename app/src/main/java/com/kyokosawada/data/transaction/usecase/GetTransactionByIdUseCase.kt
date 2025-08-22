package com.kyokosawada.data.transaction.usecase

import com.kyokosawada.data.transaction.TransactionRepository

/**
 * Use case to get a transaction (with items) by transaction id.
 */
class GetTransactionByIdUseCase(private val repo: TransactionRepository) {
    operator fun invoke(id: Long) = repo.getTransactionWithItemsById(id)
}
