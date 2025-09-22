package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Cart(
    @get:PropertyName("cart_id") @set:PropertyName("cart_id") var cart_id: Int = 0,
    @get:PropertyName("user_id") @set:PropertyName("user_id") var user_id: Int? = null,
    @get:PropertyName("product_id") @set:PropertyName("product_id") var product_id: Int? = null,
    @get:PropertyName("variant_id") @set:PropertyName("variant_id") var variant_id: Int? = null,
    @get:PropertyName("variant_sku") @set:PropertyName("variant_sku") var variant_sku: String? = null,
    @get:PropertyName("quantity") @set:PropertyName("quantity") var quantity: Int = 0,
    @get:PropertyName("added_at") @set:PropertyName("added_at") var added_at: Timestamp? = null,
    @get:PropertyName("is_active") @set:PropertyName("is_active") var is_active: Int = 1
)
