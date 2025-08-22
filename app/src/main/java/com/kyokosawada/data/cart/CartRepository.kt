package com.kyokosawada.data.cart

import kotlinx.coroutines.flow.StateFlow

/**
 * Repository interface for all cart operations.
 * Acts as a single source of truth for Cart state and operations.
 */
interface CartRepository {
    /** A StateFlow reflecting the current items in the cart. */
    val cartItems: StateFlow<List<CartItem>>

    /** Add or update the given CartItem. */
    suspend fun addOrUpdateItem(item: CartItem)

    /** Remove a CartItem by productId. */
    suspend fun removeItem(productId: Long)

    /** Update quantity of a CartItem, or remove if set to 0. */
    suspend fun updateQuantity(productId: Long, quantity: Int)

    /** Clear all items in the cart. */
    suspend fun clearCart()
}