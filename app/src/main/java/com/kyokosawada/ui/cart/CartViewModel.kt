package com.kyokosawada.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyokosawada.data.cart.CartItem
import com.kyokosawada.data.cart.usecase.AddToCartUseCase
import com.kyokosawada.data.cart.usecase.RemoveFromCartUseCase
import com.kyokosawada.data.cart.usecase.UpdateCartItemQuantityUseCase
import com.kyokosawada.data.cart.usecase.ClearCartUseCase
import com.kyokosawada.data.cart.usecase.CheckoutCartUseCase
import com.kyokosawada.data.cart.CartRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for cart UI: exposes cart state, handles all cart operations for Compose screens.
 */
class CartViewModel(
    private val cartRepository: CartRepository,
    private val addToCart: AddToCartUseCase,
    private val removeFromCart: RemoveFromCartUseCase,
    private val updateQuantity: UpdateCartItemQuantityUseCase,
    private val clearCart: ClearCartUseCase,
    private val checkoutCart: CheckoutCartUseCase
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems

    fun addItem(item: CartItem) {
        viewModelScope.launch { addToCart(item) }
    }

    fun removeItem(productId: Long) {
        viewModelScope.launch { removeFromCart(productId) }
    }

    fun updateItemQuantity(productId: Long, quantity: Int) {
        viewModelScope.launch { updateQuantity(productId, quantity) }
    }

    fun clearAll() {
        viewModelScope.launch { clearCart() }
    }

    fun checkout() {
        viewModelScope.launch { checkoutCart() }
    }
    // Add additional state/calculation for UI (e.g. totals) as needed here.
}
