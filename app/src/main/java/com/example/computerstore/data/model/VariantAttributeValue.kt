package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class VariantAttributeValue(
    @get:PropertyName("variant_id") @set:PropertyName("variant_id") var variant_id: Int = 0,
    @get:PropertyName("attribute_value_id") @set:PropertyName("attribute_value_id") var attribute_value_id: Int = 0
)
