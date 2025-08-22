package com.kyokosawada.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kyokosawada.data.product.ProductEntity
import com.kyokosawada.data.product.ProductDao
import com.kyokosawada.data.transaction.TransactionEntity
import com.kyokosawada.data.transaction.CartItemEntity
import com.kyokosawada.data.transaction.TransactionDao

/**
 * Main Room database, includes ProductEntity & ProductDao.
 */
@Database(
    entities = [ProductEntity::class, TransactionEntity::class, CartItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
}
