package com.kyokosawada.data.transaction.usecase

import com.kyokosawada.data.transaction.TransactionRepository

/**
 * Use case to get all transactions (with items) for history UI.
 */
class GetTransactionsUseCase(private val repo: TransactionRepository) {
    operator fun invoke() = repo.getAllTransactionsWithItems()
}
