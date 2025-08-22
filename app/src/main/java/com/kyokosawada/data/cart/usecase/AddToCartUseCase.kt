package com.kyokosawada.data.cart.usecase

import com.kyokosawada.data.cart.CartItem
import com.kyokosawada.data.cart.CartRepository

/**
 * Use case for adding or updating an item in the cart.
 * @property cartRepository Handles cart operations and state.
 */
class AddToCartUseCase(private val cartRepository: CartRepository) {
    /**
     * Adds or updates a product in the cart. Increases quantity if already present.
     * @param item The CartItem to add or update.
     */
    suspend operator fun invoke(item: CartItem) {
        cartRepository.addOrUpdateItem(item)
    }
}
