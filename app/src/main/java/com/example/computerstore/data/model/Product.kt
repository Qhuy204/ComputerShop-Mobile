package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Product(
    @get:PropertyName("product_id") @set:PropertyName("product_id") var product_id: Int = 0,
    @get:PropertyName("category_id") @set:PropertyName("category_id") var category_id: Int? = null,
    @get:PropertyName("product_name") @set:PropertyName("product_name") var product_name: String = "",
    @get:PropertyName("brand_name") @set:PropertyName("brand_name") var brand_name: String? = null,
    @get:PropertyName("model") @set:PropertyName("model") var model: String? = null,
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null,
    @get:PropertyName("base_price") @set:PropertyName("base_price") var base_price: Double = 0.0,
    @get:PropertyName("is_featured") @set:PropertyName("is_featured") var is_featured: Int = 0,
    @get:PropertyName("created_at") @set:PropertyName("created_at") var created_at: Timestamp? = null,
    @get:PropertyName("brand_id") @set:PropertyName("brand_id") var brand_id: Int? = null
)
