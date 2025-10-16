package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class OrderItem(
    @get:PropertyName("order_item_id") @set:PropertyName("order_item_id") var order_item_id: String? = null,
    @get:PropertyName("order_id") @set:PropertyName("order_id") var order_id: String? = null,
    @get:PropertyName("product_id") @set:PropertyName("product_id") var product_id: Int? = null,
    @get:PropertyName("variant_id") @set:PropertyName("variant_id") var variant_id: Int? = null,
    @get:PropertyName("variant_sku") @set:PropertyName("variant_sku") var variant_sku: String? = null,
    @get:PropertyName("quantity") @set:PropertyName("quantity") var quantity: Int = 0,
    @get:PropertyName("price_at_time") @set:PropertyName("price_at_time") var price_at_time: Double = 0.0
)
