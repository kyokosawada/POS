package com.kyokosawada.data.cart

import kotlinx.serialization.Serializable

/**
 * Represents an item in the shopping cart.
 * @property productId Unique identifier for the product
 * @property name Product name for display and receipts
 * @property price Unit price of the product (pre-tax, if applicable)
 * @property quantity Number of units added to the cart
 * @property subtotal Total price for this CartItem (price * quantity)
 */
@Serializable
data class CartItem(
    val productId: Long,
    val name: String,
    val price: Double,
    val quantity: Int,
    val subtotal: Double
)
