package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class ProductImage(
    @get:PropertyName("image_id") @set:PropertyName("image_id") var image_id: Int = 0,
    @get:PropertyName("product_id") @set:PropertyName("product_id") var product_id: Int? = null,
    @get:PropertyName("image_url") @set:PropertyName("image_url") var image_url: String = "",
    @get:PropertyName("is_primary") @set:PropertyName("is_primary") var is_primary: Int = 0,
    @get:PropertyName("created_at") @set:PropertyName("created_at") var created_at: Timestamp? = null
)
