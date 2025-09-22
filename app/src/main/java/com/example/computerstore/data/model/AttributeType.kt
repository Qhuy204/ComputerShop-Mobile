package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class AttributeType(
    @get:PropertyName("attribute_type_id") @set:PropertyName("attribute_type_id") var attribute_type_id: Int = 0,
    @get:PropertyName("type_name") @set:PropertyName("type_name") var type_name: String = "",
    @get:PropertyName("display_order") @set:PropertyName("display_order") var display_order: Int = 0
)
