package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class ProductSpecification(
    @get:PropertyName("spec_id") @set:PropertyName("spec_id") var spec_id: Int = 0,
    @get:PropertyName("product_id") @set:PropertyName("product_id") var product_id: Int = 0,
    @get:PropertyName("spec_name") @set:PropertyName("spec_name") var spec_name: String = "",
    @get:PropertyName("spec_value") @set:PropertyName("spec_value") var spec_value: String = "",
    @get:PropertyName("display_order") @set:PropertyName("display_order") var display_order: Int = 0
)
