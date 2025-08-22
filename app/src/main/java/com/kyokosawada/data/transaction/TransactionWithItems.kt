package com.kyokosawada.data.transaction

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Combines a TransactionEntity with its related CartItemEntity list.
 */
data class TransactionWithItems(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "transactionId"
    )
    val cartItems: List<CartItemEntity>
)
