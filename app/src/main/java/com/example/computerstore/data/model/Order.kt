package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Order(
    @get:PropertyName("order_id") @set:PropertyName("order_id") var order_id: Int = 0,
    @get:PropertyName("user_id") @set:PropertyName("user_id") var user_id: Int? = null,
    @get:PropertyName("guest_email") @set:PropertyName("guest_email") var guest_email: String? = null,
    @get:PropertyName("guest_phone") @set:PropertyName("guest_phone") var guest_phone: String? = null,
    @get:PropertyName("guest_name") @set:PropertyName("guest_name") var guest_name: String? = null,
    @get:PropertyName("total_amount") @set:PropertyName("total_amount") var total_amount: Double = 0.0,
    @get:PropertyName("order_date") @set:PropertyName("order_date") var order_date: Timestamp? = null,
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "Pending",
    @get:PropertyName("shipping_address") @set:PropertyName("shipping_address") var shipping_address: String = "",
    @get:PropertyName("payment_method_id") @set:PropertyName("payment_method_id") var payment_method_id: Int? = null,
    @get:PropertyName("payment_status") @set:PropertyName("payment_status") var payment_status: String = "Pending",
    @get:PropertyName("note") @set:PropertyName("note") var note: String? = null
)
