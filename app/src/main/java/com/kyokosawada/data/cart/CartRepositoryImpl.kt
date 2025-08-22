package com.kyokosawada.data.cart

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Concrete implementation of CartRepository for local/in-memory cart state management.
 */
class CartRepositoryImpl : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> = _cartItems

    override suspend fun addOrUpdateItem(item: CartItem) {
        val currentList = _cartItems.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.productId == item.productId }
        if (existingIndex >= 0) {
            val updated = item.copy(
                quantity = currentList[existingIndex].quantity + item.quantity,
                subtotal = item.price * (currentList[existingIndex].quantity + item.quantity)
            )
            currentList[existingIndex] = updated
        } else {
            currentList.add(item.copy(subtotal = item.price * item.quantity))
        }
        _cartItems.value = currentList
    }

    override suspend fun removeItem(productId: Long) {
        _cartItems.value = _cartItems.value.filter { it.productId != productId }
    }

    override suspend fun updateQuantity(productId: Long, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val idx = currentList.indexOfFirst { it.productId == productId }
        if (idx >= 0) {
            if (quantity <= 0) {
                currentList.removeAt(idx)
            } else {
                val item = currentList[idx]
                currentList[idx] = item.copy(quantity = quantity, subtotal = item.price * quantity)
            }
        }
        _cartItems.value = currentList
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }
}