package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class PaymentMethod(
    @get:PropertyName("payment_method_id") @set:PropertyName("payment_method_id") var payment_method_id: Int = 0,
    @get:PropertyName("payment_method_name") @set:PropertyName("payment_method_name") var payment_method_name: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null
)
