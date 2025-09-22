package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class CategoryAttribute(
    @get:PropertyName("category_id") @set:PropertyName("category_id") var category_id: Int = 0,
    @get:PropertyName("attribute_type_id") @set:PropertyName("attribute_type_id") var attribute_type_id: Int = 0,
    @get:PropertyName("is_required") @set:PropertyName("is_required") var is_required: Int = 0
)
