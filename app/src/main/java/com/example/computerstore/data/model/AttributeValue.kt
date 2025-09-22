package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class AttributeValue(
    @get:PropertyName("attribute_value_id") @set:PropertyName("attribute_value_id") var attribute_value_id: Int = 0,
    @get:PropertyName("attribute_type_id") @set:PropertyName("attribute_type_id") var attribute_type_id: Int = 0,
    @get:PropertyName("value_name") @set:PropertyName("value_name") var value_name: String = ""
)
