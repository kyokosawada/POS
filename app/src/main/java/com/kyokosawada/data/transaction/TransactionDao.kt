package com.kyokosawada.data.transaction

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Transaction and related CartItems; supports history and receipt logic.
 */
@Dao
interface TransactionDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItems(items: List<CartItemEntity>)

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsWithItems(): Flow<List<TransactionWithItems>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionWithItemsById(id: Long): Flow<TransactionWithItems?>
}
