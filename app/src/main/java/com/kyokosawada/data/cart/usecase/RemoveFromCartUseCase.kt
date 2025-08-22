package com.kyokosawada.data.cart.usecase

import com.kyokosawada.data.cart.CartRepository

/**
 * Use case for removing an item from the cart.
 * @property cartRepository Handles cart operations and state.
 */
class RemoveFromCartUseCase(private val cartRepository: CartRepository) {
    /**
     * Removes the item with the given productId from the cart.
     * @param productId The product ID to remove.
     */
    suspend operator fun invoke(productId: Long) {
        cartRepository.removeItem(productId)
    }
}
