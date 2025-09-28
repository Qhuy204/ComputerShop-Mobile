package com.example.computerstore.data.model

data class CartItemUI(
    val cart: Cart,
    val product: Product?,
    val variant: ProductVariant?,
    val imageUrl: String?
)
