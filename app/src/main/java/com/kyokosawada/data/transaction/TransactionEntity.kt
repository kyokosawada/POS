package com.kyokosawada.data.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Transaction entity for historical sales; Room persistence.
 * Follows MVVM and PRD spec for data.
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: Long = System.currentTimeMillis(),
    val total: Double,
    val paymentType: String,
    val receiptPdfUri: String? = null
)
