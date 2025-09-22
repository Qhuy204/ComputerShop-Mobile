package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class ProductVariant(
    @get:PropertyName("variant_id") @set:PropertyName("variant_id") var variant_id: Int = 0,
    @get:PropertyName("product_id") @set:PropertyName("product_id") var product_id: Int = 0,
    @get:PropertyName("variant_sku") @set:PropertyName("variant_sku") var variant_sku: String = "",
    @get:PropertyName("price_adjustment") @set:PropertyName("price_adjustment") var price_adjustment: Double = 0.0,
    @get:PropertyName("stock_quantity") @set:PropertyName("stock_quantity") var stock_quantity: Int = 0,
    @get:PropertyName("is_default") @set:PropertyName("is_default") var is_default: Int = 0,
    @get:PropertyName("image_url") @set:PropertyName("image_url") var image_url: String? = null
)
