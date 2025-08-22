package com.kyokosawada.data.product

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Product entity for inventory management, Room integration (See PRD).
 * MVVM Domain Model.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val price: Double,
    val sku: String,
    val barcode: String,
    val stockQty: Int,
    val category: String = "",
    val imageUrl: String? = null,
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis()
)
