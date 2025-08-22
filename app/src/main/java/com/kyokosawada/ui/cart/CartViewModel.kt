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
import com.kyokosawada.data.transaction.usecase.CreateTransactionUseCase
import com.kyokosawada.data.transaction.TransactionEntity
import com.kyokosawada.data.transaction.CartItemEntity
import com.kyokosawada.data.product.ProductRepository
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
    private val checkoutCart: CheckoutCartUseCase,
    private val createTransaction: CreateTransactionUseCase, // <-- inject for real checkout
    private val productRepository: ProductRepository
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

    // Now takes paymentType, robust transaction + cart clear
    fun checkout(paymentType: String = "cash", onError: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            val items = cartItems.value
            if (items.isNotEmpty()) {
                // Final stock validation: do not allow oversell
                val outOfStock = mutableListOf<String>()
                items.forEach { cartItem ->
                    val product = productRepository.getProductById(cartItem.productId)
                    if (product == null || cartItem.quantity > product.stockQty) {
                        outOfStock.add(cartItem.name)
                    }
                }
                if (outOfStock.isNotEmpty()) {
                    onError?.invoke("Not enough stock for: ${outOfStock.joinToString()}")
                    return@launch
                }
                val total = items.sumOf { it.subtotal }
                val transaction = TransactionEntity(
                    total = total,
                    paymentType = paymentType
                )
                val cartEntities = items.map {
                    CartItemEntity(
                        productId = it.productId,
                        transactionId = 0L, // will be filled in by repo after insert
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity,
                        subtotal = it.subtotal
                    )
                }
                createTransaction(transaction, cartEntities)
                // Decrement product stock after transaction
                items.forEach { cartItem ->
                    val product = productRepository.getProductById(cartItem.productId)
                    if (product != null && product.stockQty >= cartItem.quantity) {
                        val updated = product.copy(
                            stockQty = product.stockQty - cartItem.quantity,
                            updated = System.currentTimeMillis()
                        )
                        productRepository.updateProduct(updated)
                    }
                }
                clearCart()
            }
        }
    }
    // Add additional state/calculation for UI (e.g. totals) as needed here.
}
