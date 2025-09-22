package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class Category(
    @get:PropertyName("category_id") @set:PropertyName("category_id") var category_id: Int = 0,
    @get:PropertyName("category_name") @set:PropertyName("category_name") var category_name: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null
)
