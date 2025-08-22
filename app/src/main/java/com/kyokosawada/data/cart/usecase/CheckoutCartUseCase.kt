package com.kyokosawada.data.cart.usecase

import com.kyokosawada.data.cart.CartRepository

/**
 * Use case for handling checkout: decrements stock and clears cart.
 * @property cartRepository Handles cart operations and state.
 */
class CheckoutCartUseCase(private val cartRepository: CartRepository) {
    /**
     * Performs checkout, decrements stock (delegated elsewhere), then clears cart.
     * Typically triggers receipt generation in upper layers.
     */
    suspend operator fun invoke() {
        // Implementation will call repo logic to adjust stock and clear cart after payment.
        // Stock decrement should be handled in domain/repo based on cartItems.
        cartRepository.clearCart()
    }
}
