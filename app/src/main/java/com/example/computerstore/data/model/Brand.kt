package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class Brand(
    @get:PropertyName("brand_id") @set:PropertyName("brand_id") var brand_id: Int = 0,
    @get:PropertyName("brand_name") @set:PropertyName("brand_name") var brand_name: String = ""
)
