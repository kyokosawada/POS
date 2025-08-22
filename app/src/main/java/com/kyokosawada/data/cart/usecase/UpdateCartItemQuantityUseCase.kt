package com.kyokosawada.data.cart.usecase

import com.kyokosawada.data.cart.CartRepository

/**
 * Use case for updating the quantity of an item in the cart.
 * @property cartRepository Handles cart operations and state.
 */
class UpdateCartItemQuantityUseCase(private val cartRepository: CartRepository) {
    /**
     * Updates the quantity of the given item (removes if quantity is zero).
     * @param productId The product ID for update.
     * @param quantity The new quantity value.
     */
    suspend operator fun invoke(productId: Long, quantity: Int) {
        cartRepository.updateQuantity(productId, quantity)
    }
}
