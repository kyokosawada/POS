package com.kyokosawada.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kyokosawada.data.product.ProductEntity
import com.kyokosawada.data.product.ProductDao

/**
 * Main Room database, includes ProductEntity & ProductDao.
 */
@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
