package com.kyokosawada.data.cart.usecase

import com.kyokosawada.data.cart.CartRepository

/**
 * Use case for clearing all items in the cart.
 * @property cartRepository Handles cart operations and state.
 */
class ClearCartUseCase(private val cartRepository: CartRepository) {
    /**
     * Clears all items in the cart.
     */
    suspend operator fun invoke() {
        cartRepository.clearCart()
    }
}
